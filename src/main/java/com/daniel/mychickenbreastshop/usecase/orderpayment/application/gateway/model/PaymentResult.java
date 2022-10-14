package com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.model;

/**
 * 전략에 대한 조회 결과
 */
public interface PaymentResult {

    String getRedirectUrl();
}
