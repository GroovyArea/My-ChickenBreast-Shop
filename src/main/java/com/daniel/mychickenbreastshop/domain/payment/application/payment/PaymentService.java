package com.daniel.mychickenbreastshop.domain.payment.application.payment;

import com.daniel.mychickenbreastshop.domain.payment.domain.pay.model.PaymentApi;

/**
 * 결제 API 서비스 인터페이스
 */
public interface PaymentService {

    PaymentApi getPaymentApiName();

}
