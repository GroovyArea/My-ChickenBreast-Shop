package com.daniel.mychickenbreastshop.domain.payment.application.payment.strategy.service;

import com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.application.KakaoPaymentService;
import com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.webclient.model.KakaoPayResponse.OrderInfoResponse;
import com.daniel.mychickenbreastshop.domain.payment.application.payment.strategy.model.PaymentResult;
import com.daniel.mychickenbreastshop.domain.payment.domain.order.OrderRepository;
import com.daniel.mychickenbreastshop.domain.payment.domain.order.OrderedProductRepository;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.CardRepository;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.PayRepository;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.request.ItemPayRequestDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.request.PayCancelRequestDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.model.PaymentApi;
import com.daniel.mychickenbreastshop.domain.product.domain.item.ProductRepository;
import com.daniel.mychickenbreastshop.domain.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.daniel.mychickenbreastshop.domain.payment.domain.pay.model.PaymentApi.KAKAO;

@Service
@RequiredArgsConstructor
public class KakaopayStrategyApplication implements PaymentStrategyApplication<PaymentResult>{

    private final KakaoPaymentService kakaoPaymentService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final PayRepository payRepository;
    private final OrderedProductRepository orderedProductRepository;
    private final CardRepository cardRepository;

    @Override
    public PaymentApi getPaymentApiName() {
        return KAKAO;
    }

    @Override
    public OrderInfoResponse getOrderInfo() {
        return null;
    }

    @Override
    public PaymentResult payItem(ItemPayRequestDto itemPayRequestDto, String requestUrl, String loginId) {
        return kakaoPaymentService.payItem(itemPayRequestDto, requestUrl, loginId);
    }

    @Override
    public PaymentResult payCart(String cookieValue, String requestUrl, String loginId) {
        return kakaoPaymentService.payCart(cookieValue, requestUrl, loginId);
    }

    @Override
    public PaymentResult completePayment(String payToken, String loginId) {
        return kakaoPaymentService.completePayment(payToken, loginId);
    }

    @Override
    public PaymentResult cancelPayment(PayCancelRequestDto payCancelRequestDto) {
        return kakaoPaymentService.cancelPayment(payCancelRequestDto);
    }
}
