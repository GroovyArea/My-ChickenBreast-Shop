package com.daniel.mychickenbreastshop.domain.payment.application.payment.strategy.service;

import com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.application.KakaoPaymentService;
import com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.webclient.model.KakaoPayResponse;
import com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.webclient.model.KakaoPayResponse.OrderInfoResponse;
import com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.webclient.model.KakaoPayResponse.PayReadyResponse;
import com.daniel.mychickenbreastshop.domain.payment.application.payment.strategy.model.PaymentResult;
import com.daniel.mychickenbreastshop.domain.payment.domain.order.Order;
import com.daniel.mychickenbreastshop.domain.payment.domain.order.OrderProduct;
import com.daniel.mychickenbreastshop.domain.payment.domain.order.OrderRepository;
import com.daniel.mychickenbreastshop.domain.payment.domain.order.model.OrderStatus;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.Card;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.Payment;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.PaymentRepository;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.request.ItemPayRequestDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.request.PayCancelRequestDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.model.PayStatus;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.model.PaymentApi;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.model.PaymentResponse;
import com.daniel.mychickenbreastshop.domain.payment.extract.CartDisassembler;
import com.daniel.mychickenbreastshop.domain.payment.extract.model.CartItem;
import com.daniel.mychickenbreastshop.domain.payment.extract.model.CartValue;
import com.daniel.mychickenbreastshop.domain.product.domain.item.Product;
import com.daniel.mychickenbreastshop.domain.product.domain.item.ProductRepository;
import com.daniel.mychickenbreastshop.domain.product.domain.item.model.ProductResponse;
import com.daniel.mychickenbreastshop.domain.user.domain.User;
import com.daniel.mychickenbreastshop.domain.user.domain.UserRepository;
import com.daniel.mychickenbreastshop.domain.user.domain.model.UserResponse;
import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.webclient.model.KakaoPayResponse.PayApproveResponse;
import static com.daniel.mychickenbreastshop.domain.payment.domain.pay.model.PaymentApi.KAKAO;

/**
 * 전략 결제 API 서비스 구현체 (카카오페이)
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KakaopayStrategyApplication implements PaymentStrategyApplication<PaymentResult> {

    private final KakaoPaymentService kakaoPaymentService;
    private final CartDisassembler cartDisassembler;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository payRepository;

    @Override
    public PaymentApi getPaymentApiName() {
        return KAKAO;
    }

    @Override
    public OrderInfoResponse getOrderInfo() {
        return null;
    }

    /**
     * 추후 redisson lock 사용
     */
    @Override
    @Transactional
    public PaymentResult payItem(ItemPayRequestDto itemPayRequestDto, String requestUrl, String loginId) {
        PayReadyResponse response = kakaoPaymentService.payItem(itemPayRequestDto, requestUrl, loginId);

        User savedUser = getSavedUser(loginId);
        Product savedProduct = getSavedProduct(itemPayRequestDto.getItemName());

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
    @Transactional
    public PaymentResult payCart(String cookieValue, String requestUrl, String loginId) {
        CartValue cartValue = getCartValue(cookieValue);
        PayReadyResponse response = kakaoPaymentService.payCart(cartValue, requestUrl, loginId);

        User savedUser = getSavedUser(loginId);
        List<Product> savedProducts = cartValue.getItemNames().stream().map(this::getSavedProduct).toList();

        Order order = Order.createReadyOrder(savedProducts.size(), cartValue.getTotalPrice(), savedUser);

        List<OrderProduct> orderProducts = new ArrayList<>();

        int bound = savedProducts.size();
        for (int i = 0; i < bound; i++) {
            orderProducts.add(
                    OrderProduct.createOrderProduct(
                            savedProducts.get(i).getQuantity(),
                            savedProducts.get(i).getName(),
                            savedProducts.get(i).getPrice(),
                            savedProducts.get(i).getImage(),
                            savedProducts.get(i).getContent()));
            order.addOrderProduct(orderProducts.get(i));
        }

        /* 재고 차감 로직 만들기 */
        Payment payment = Payment.builder()
                .totalPrice(cartValue.getTotalPrice())
                .status(PayStatus.READY)
                .build();

        order.setPaymentInfo(payment);

        orderRepository.save(order);

        return response;
    }

    /**
     * 재고 차감을 주문 성공하고 나서? 추후 redissonLock 사용해서 update lock 걸기
     */
    @Override
    @Transactional
    public PaymentResult completePayment(String payToken, String loginId) {
        PayApproveResponse response = kakaoPaymentService.completePayment(payToken, loginId);

        Payment savedPayment = payRepository.findByPgToken(payToken).orElseThrow(() -> new BadRequestException(PaymentResponse.PAYMENT_NOT_EXISTS.getMessage()));

        if (response.getCardInfo() != null) {
            Card card = Card.createCard(
                    response.getCardInfo().getBin(),
                    response.getCardInfo().getCardType(),
                    response.getCardInfo().getInstallMonth(),
                    response.getCardInfo().getInterestFreeInstall());

            savedPayment.setCardInfo(card);
        }

        savedPayment.updatePaymentStatus(PayStatus.COMPLETED);
        savedPayment.getOrder().updateOrderStatus(OrderStatus.ORDER_APPROVAL);

        return response;
    }

    @Override
    @Transactional
    public PaymentResult cancelPayment(PayCancelRequestDto payCancelRequestDto) {
        KakaoPayResponse.PayCancelResponse response = kakaoPaymentService.cancelPayment(payCancelRequestDto);

        Payment savedPayment = payRepository.findByPgToken(payCancelRequestDto.getPayId()).orElseThrow(() -> new BadRequestException(PaymentResponse.PAYMENT_NOT_EXISTS.getMessage()));

        savedPayment.updatePaymentStatus(PayStatus.CANCELED);
        savedPayment.getOrder().updateOrderStatus(OrderStatus.CANCEL_ORDER);

        return response;
    }

    private Product getSavedProduct(String productName) {
        return productRepository.findByName(productName).orElseThrow(
                () -> new BadRequestException(ProductResponse.ITEM_NOT_EXISTS.getMessage()));
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
