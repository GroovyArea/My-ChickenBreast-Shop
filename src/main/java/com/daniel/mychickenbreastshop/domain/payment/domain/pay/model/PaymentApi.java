package com.daniel.mychickenbreastshop.domain.payment.domain.pay.model;

import com.daniel.mychickenbreastshop.domain.payment.application.payment.PaymentService;
import com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.application.KakaoPaymentService;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum PaymentApi {

    KAKAO(KakaoPaymentService.class); // 카카오페이 결제 API

    private final Class<? extends PaymentService> paymentType;

    public static Class<? extends PaymentService> getType(PaymentApi paymentApi) {
        return Arrays.stream(PaymentApi.values())
                .filter(paymentApi1 -> paymentApi1 == paymentApi)
                .findFirst()
                .orElse(KAKAO).getPaymentType();
    }

}
