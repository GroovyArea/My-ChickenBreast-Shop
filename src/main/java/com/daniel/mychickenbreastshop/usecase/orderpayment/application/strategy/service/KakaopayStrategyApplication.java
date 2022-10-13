package com.daniel.mychickenbreastshop.usecase.orderpayment.application.strategy.service;

import com.daniel.mychickenbreastshop.domain.order.model.Order;
import com.daniel.mychickenbreastshop.domain.order.model.OrderProduct;
import com.daniel.mychickenbreastshop.domain.order.model.OrderRepository;
import com.daniel.mychickenbreastshop.domain.order.model.model.OrderStatus;
import com.daniel.mychickenbreastshop.domain.payment.model.Card;
import com.daniel.mychickenbreastshop.domain.payment.model.Payment;
import com.daniel.mychickenbreastshop.domain.payment.model.enums.PayStatus;
import com.daniel.mychickenbreastshop.domain.payment.model.enums.PaymentType;
import com.daniel.mychickenbreastshop.domain.product.model.item.Product;
import com.daniel.mychickenbreastshop.domain.product.model.item.ProductRepository;
import com.daniel.mychickenbreastshop.domain.user.model.User;
import com.daniel.mychickenbreastshop.domain.user.model.UserRepository;
import com.daniel.mychickenbreastshop.domain.user.model.model.UserResponse;
import com.daniel.mychickenbreastshop.global.aspect.annotation.RedisLocked;
import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.application.KakaoPaymentService;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient.model.KakaoPayResponse.PayCancelResponse;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient.model.KakaoPayResponse.PayReadyResponse;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.strategy.model.PaymentResult;
import com.daniel.mychickenbreastshop.usecase.orderpayment.extract.CartDisassembler;
import com.daniel.mychickenbreastshop.usecase.orderpayment.extract.model.CartItem;
import com.daniel.mychickenbreastshop.usecase.orderpayment.extract.model.CartValue;
import com.daniel.mychickenbreastshop.usecase.orderpayment.model.dto.request.ItemPayRequestDto;
import com.daniel.mychickenbreastshop.usecase.orderpayment.model.dto.request.PayCancelRequestDto;
import com.daniel.mychickenbreastshop.usecase.orderpayment.model.enums.PaymentGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static com.daniel.mychickenbreastshop.domain.product.model.item.model.ProductResponse.ITEM_NOT_EXISTS;
import static com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient.model.KakaoPayResponse.PayApproveResponse;
import static com.daniel.mychickenbreastshop.usecase.orderpayment.model.enums.PaymentGateway.KAKAO;

/**
 * 전략 결제 API 서비스 구현체 (카카오페이)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class KakaopayStrategyApplication implements PaymentStrategyApplication<PaymentResult> {

    private final KakaoPaymentService kakaoPaymentService;
    private final CartDisassembler cartDisassembler;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Override
    public PaymentGateway getPaymentGatewayName() {
        return KAKAO;
    }

    @Override
    @RedisLocked(key = "payItem")
    @Transactional
    public PaymentResult payItem(ItemPayRequestDto itemPayRequestDto, String requestUrl, String loginId) {
        Product savedProduct = getSavedProduct(itemPayRequestDto.getItemName());
        savedProduct.checkStockQuantity(itemPayRequestDto.getQuantity());

        PayReadyResponse response = kakaoPaymentService.payItem(itemPayRequestDto, requestUrl, loginId);

        User savedUser = getSavedUser(loginId);

        Order order = Order.createReadyOrder(itemPayRequestDto.getQuantity(), itemPayRequestDto.getTotalAmount(),
                savedUser);

        OrderProduct orderProduct = OrderProduct.createOrderProduct(
                itemPayRequestDto.getQuantity(),
                savedProduct.getName(),
                savedProduct.getPrice(),
                savedProduct.getImage(),
                savedProduct.getContent()
        );

        Payment payment = Payment.builder()
                .totalPrice(Long.valueOf(itemPayRequestDto.getTotalAmount()))
                .status(PayStatus.READY)
                .build();

        order.addOrderProduct(orderProduct);
        order.setPaymentInfo(payment);

        orderRepository.save(order);

        return response;
    }

    @Override
    @RedisLocked(key = "payCart")
    @Transactional
    public PaymentResult payCart(String cookieValue, String requestUrl, String loginId) {
        CartValue cartValue = getCartValue(cookieValue);

        User savedUser = getSavedUser(loginId);
        List<Product> savedProducts = cartValue.getItemNames().stream().map(this::getSavedProduct).toList();

        for (int i = 0; i < savedProducts.size(); i++) {
            savedProducts.get(i).checkStockQuantity(cartValue.getItemQuantities().get(i));
        }

        PayReadyResponse response = kakaoPaymentService.payCart(cartValue, requestUrl, loginId);

        Order order = Order.createReadyOrder(savedProducts.size(), cartValue.getTotalPrice(), savedUser);

        savedProducts.stream().map(savedProduct -> OrderProduct.createOrderProduct(
                savedProduct.getQuantity(),
                savedProduct.getName(),
                savedProduct.getPrice(),
                savedProduct.getImage(),
                savedProduct.getContent())).forEach(order::addOrderProduct);

        Payment payment = Payment.builder()
                .totalPrice(cartValue.getTotalPrice())
                .status(PayStatus.READY)
                .card(null)
                .build();

        order.setPaymentInfo(payment);

        orderRepository.save(order);

        return response;
    }

    @Override
    @RedisLocked(key = "completePayment")
    @Transactional
    public PaymentResult completePayment(String payToken, String loginId) {
        PayApproveResponse response = kakaoPaymentService.completePayment(payToken, loginId);
        User savedUser = getSavedUser(loginId);

        Payment savedPayment = savedUser.getOrders().get(savedUser.getOrders().size() - 1).getPayment();

        if (response.getCardInfo() != null) {
            Card card = Card.createCard(
                    response.getCardInfo().getBin(),
                    response.getCardInfo().getCardType(),
                    response.getCardInfo().getInstallMonth(),
                    response.getCardInfo().getInterestFreeInstall());

            savedPayment.updatePaymentTypeInfo(PaymentType.CARD);
            savedPayment.setCardInfo(card);

        } else {
            savedPayment.updatePaymentTypeInfo(PaymentType.CASH);
        }

        savedPayment.updatePaymentStatus(PayStatus.COMPLETED);
        savedPayment.getOrder().updateOrderStatus(OrderStatus.ORDER_COMPLETE);

        /* 재고 차감 */
        quantityDecrease(response);

        return response;
    }

    @Override
    @RedisLocked(key = "cancelPayment")
    @Transactional
    public PaymentResult cancelPayment(PayCancelRequestDto payCancelRequestDto, String loginId) {
        PayCancelResponse response = kakaoPaymentService.cancelPayment(payCancelRequestDto);

        User savedUser = getSavedUser(loginId);
        Payment savedPayment = savedUser.getOrders().get(savedUser.getOrders().size() - 1).getPayment();

        savedPayment.updatePaymentStatus(PayStatus.CANCELED);
        savedPayment.getOrder().updateOrderStatus(OrderStatus.CANCEL_ORDER);

        /* 재고 회복 */
        quantityIncrease(response);

        return response;
    }

    @RedisLocked(key = "test")
    @Transactional
    public void test(String id) {
        log.info("일해라!");
        Product product = productRepository.findById(1L).orElseThrow(() -> new RuntimeException("에헤이"));
        log.info("상품 전 개수:" + product.getQuantity());
        product.decreaseItemQuantity(1);
        log.info("상품 후 개수:" + product.getQuantity());
    }

    private void quantityIncrease(PayCancelResponse response) {
        String itemCode = response.getItemCode();

        if (itemCode.isEmpty()) { // 다중 상품 결제 시
            String[] itemCodes = getItemCodes(itemCode);
            List<String> itemNumbers = getItemNumbers(itemCodes);
            List<String> itemQuantities = getItemQuantities(itemCodes);

            for (int i = 0; i < itemNumbers.size(); i++) {
                Product savedProduct = productRepository.findById(Long.valueOf(itemNumbers.get(i))).orElseThrow(() -> new BadRequestException(ITEM_NOT_EXISTS.getMessage()));
                savedProduct.increaseItemQuantity(Integer.parseInt(itemQuantities.get(i)));
            }
        } else { // 단일 상품 결제 시
            Product savedProduct = productRepository.findByName(response.getItemName()).orElseThrow(() -> new BadRequestException(ITEM_NOT_EXISTS.getMessage()));
            savedProduct.increaseItemQuantity(response.getQuantity());
        }
    }


    private void quantityDecrease(PayApproveResponse response) {
        String itemCode = response.getItemCode();

        if (!itemCode.isEmpty()) { // 다중 상품 결제 시
            String[] itemCodes = getItemCodes(itemCode);
            List<String> itemNumbers = getItemNumbers(itemCodes);
            List<String> itemQuantities = getItemQuantities(itemCodes);

            for (int i = 0; i < itemNumbers.size(); i++) {
                Product savedProduct = productRepository.findById(Long.valueOf(itemNumbers.get(i))).orElseThrow(() -> new BadRequestException(ITEM_NOT_EXISTS.getMessage()));
                savedProduct.decreaseItemQuantity(Integer.parseInt(itemQuantities.get(i)));
            }
        } else { // 단일 상품 결제 시
            Product savedProduct = productRepository.findByName(response.getItemName()).orElseThrow(() -> new BadRequestException(ITEM_NOT_EXISTS.getMessage()));
            savedProduct.decreaseItemQuantity(response.getQuantity());
        }
    }

    private String[] getItemCodes(String itemCode) {
        return itemCode.split("/");
    }

    private List<String> getItemQuantities(String[] itemCodes) {
        return Arrays.stream(itemCodes[1].split(",")).toList();
    }

    private List<String> getItemNumbers(String[] itemCodes) {
        return Arrays.stream(itemCodes[0].split(",")).toList();
    }

    private Product getSavedProduct(String productName) {
        return productRepository.findByName(productName).orElseThrow(
                () -> new BadRequestException(ITEM_NOT_EXISTS.getMessage()));
    }

    private User getSavedUser(String loginId) {
        return userRepository.findByLoginId(loginId).orElseThrow(
                () -> new BadRequestException(UserResponse.USER_NOT_EXISTS.getMessage()));
    }

    private CartValue getCartValue(String cookieValue) {
        return CartValue.builder()
                .itemNumbers(cartDisassembler.getItemNumbers(cookieValue, Long.class, CartItem.class, CartItem::getItemNo))
                .itemNames(cartDisassembler.getItemNames(cookieValue, Long.class, CartItem.class, CartItem::getItemName))
                .itemQuantities(cartDisassembler.getItemQuantities(cookieValue, Long.class, CartItem.class, CartItem::getItemQuantity))
                .totalPrice(cartDisassembler.getTotalPrice(cookieValue, Long.class, CartItem.class, CartItem::getTotalPrice))
                .build();
    }

}
