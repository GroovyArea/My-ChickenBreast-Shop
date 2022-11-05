package com.daniel.mychickenbreastshop.usecase.orderpayment.application.strategy.service.kakaopay;

import com.daniel.mychickenbreastshop.domain.order.model.Order;
import com.daniel.mychickenbreastshop.domain.order.model.OrderProduct;
import com.daniel.mychickenbreastshop.domain.order.model.OrderRepository;
import com.daniel.mychickenbreastshop.domain.order.model.enums.OrderStatus;
import com.daniel.mychickenbreastshop.domain.payment.model.Card;
import com.daniel.mychickenbreastshop.domain.payment.model.Payment;
import com.daniel.mychickenbreastshop.domain.payment.model.enums.PayStatus;
import com.daniel.mychickenbreastshop.domain.payment.model.enums.PaymentType;
import com.daniel.mychickenbreastshop.domain.product.model.item.Product;
import com.daniel.mychickenbreastshop.domain.product.model.item.ProductRepository;
import com.daniel.mychickenbreastshop.domain.user.model.User;
import com.daniel.mychickenbreastshop.domain.user.model.UserRepository;
import com.daniel.mychickenbreastshop.domain.user.model.enums.UserResponse;
import com.daniel.mychickenbreastshop.global.aspect.annotation.RedisLocked;
import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.application.KakaoPaymentService;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient.model.KakaoPayResponse.PayCancelResponse;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient.model.KakaoPayResponse.PayReadyResponse;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.model.PaymentResult;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.strategy.service.PaymentStrategyApplication;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.strategy.service.adjust.ItemQuantityAdjuster;
import com.daniel.mychickenbreastshop.usecase.orderpayment.extract.CartDisassembler;
import com.daniel.mychickenbreastshop.usecase.orderpayment.extract.model.CartValue;
import com.daniel.mychickenbreastshop.usecase.orderpayment.model.dto.request.ItemPayRequestDto;
import com.daniel.mychickenbreastshop.usecase.orderpayment.model.dto.request.PayCancelRequestDto;
import com.daniel.mychickenbreastshop.usecase.orderpayment.model.enums.PaymentGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.daniel.mychickenbreastshop.domain.product.model.item.enums.ProductResponse.ITEM_NOT_EXISTS;
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
    private final ItemQuantityAdjuster kakaopayItemQuantityAdjuster;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Override
    public PaymentGateway getPaymentGatewayName() {
        return KAKAO;
    }

    @Override
    public PaymentResult getOrderInfo(String franchiseeId, String payId, String requestUrl) {
        return kakaoPaymentService.getOrderInfo(franchiseeId, payId, requestUrl);
    }

    @Override
    @RedisLocked(lockKey = "payItem")
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
        Payment payment = Payment.createPayment(itemPayRequestDto.getTotalAmount());

        order.addOrderProduct(orderProduct);
        order.setPaymentInfo(payment);
        orderRepository.save(order);

        return response;
    }

    @Override
    @RedisLocked(lockKey = "payCart")
    @Transactional
    public PaymentResult payCart(String cookieValue, String requestUrl, String loginId) {
        CartValue cartValue = cartDisassembler.getCartValue(cookieValue);

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

        Payment payment = Payment.createPayment(cartValue.getTotalPrice());

        order.setPaymentInfo(payment);
        orderRepository.save(order);

        return response;
    }

    @Override
    @RedisLocked(lockKey = "completePayment")
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
            savedPayment.updateCardInfo(card);
        } else {
            savedPayment.updatePaymentTypeInfo(PaymentType.CASH);
        }

        savedPayment.updatePaymentStatus(PayStatus.COMPLETED);
        savedPayment.getOrder().updateOrderStatus(OrderStatus.ORDER_COMPLETE);

        kakaopayItemQuantityAdjuster.quantityDecrease(response.getItemCode(), response.getItemName(), response.getQuantity());

        return response;
    }

    @Override
    @RedisLocked(lockKey = "cancelPayment")
    @Transactional
    public PaymentResult cancelPayment(PayCancelRequestDto payCancelRequestDto, String loginId) {
        PayCancelResponse response = kakaoPaymentService.cancelPayment(payCancelRequestDto);

        User savedUser = getSavedUser(loginId);
        Payment savedPayment = savedUser.getOrders().get(savedUser.getOrders().size() - 1).getPayment();

        savedPayment.updatePaymentStatus(PayStatus.CANCELED);
        savedPayment.getOrder().updateOrderStatus(OrderStatus.CANCEL_ORDER);

        kakaopayItemQuantityAdjuster.quantityIncrease(response.getItemCode(), response.getItemName(), response.getQuantity());

        return response;
    }

    @RedisLocked(lockKey = "test")
    @Transactional
    public void test(String id) {
        log.info("일해라!");
        Product product = productRepository.findById(1L).orElseThrow(() -> new RuntimeException("에헤이"));
        log.info("상품 전 개수:" + product.getQuantity());
        product.decreaseItemQuantity(1);
        log.info("상품 후 개수:" + product.getQuantity());
    }

    private Product getSavedProduct(String productName) {
        return productRepository.findByName(productName).orElseThrow(
                () -> new BadRequestException(ITEM_NOT_EXISTS.getMessage()));
    }

    private User getSavedUser(String loginId) {
        return userRepository.findByLoginId(loginId).orElseThrow(
                () -> new BadRequestException(UserResponse.USER_NOT_EXISTS.getMessage()));
    }

}