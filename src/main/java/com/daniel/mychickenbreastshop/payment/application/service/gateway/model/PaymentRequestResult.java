package com.daniel.mychickenbreastshop.payment.application.service.gateway.model;

/**
 * 결제 요청에 관한 결과
 */
public interface PaymentRequestResult extends PaymentResult{

    String getRedirectUrl();
    Long getPaymentId();
}
