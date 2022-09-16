package com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay;

import com.daniel.mychickenbreastshop.domain.payment.application.payment.PaymentApplicant;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.request.ItemPayRequestDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.request.PayCancelRequestDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.model.PaymentApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.daniel.mychickenbreastshop.domain.payment.domain.pay.model.PaymentApi.*;

/**
 * 카카오 페이 API 요청 객체
 */
@Component
@RequiredArgsConstructor
public class KakaoPayApplicant implements PaymentApplicant {

    @Override
    public PaymentApi getPaymentApiName() {
        return KAKAO;
    }

    @Override
    public String payItem(ItemPayRequestDto itemPayRequestDto, String requestURL, Long userId) {
        return null;
    }

    @Override
    public String payCart(String cookieValue, String requestURL, Long userId) {
        return null;
    }

    @Override
    public void completePayment(String payToken) {

    }

    @Override
    public void cancelPayment(PayCancelRequestDto payCancelRequestDto) {

    }
}
