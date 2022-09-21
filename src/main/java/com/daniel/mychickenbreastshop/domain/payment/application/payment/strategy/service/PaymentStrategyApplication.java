package com.daniel.mychickenbreastshop.domain.payment.application.payment.strategy.service;

import com.daniel.mychickenbreastshop.domain.payment.application.payment.strategy.model.PaymentResult;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.request.ItemPayRequestDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.request.PayCancelRequestDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.model.PaymentApi;

/**
 * 결제 API 서비스 전략
 */
public interface PaymentStrategyApplication <T extends PaymentResult> {

    PaymentApi getPaymentApiName();

    T getOrderInfo();

    T payItem(ItemPayRequestDto itemPayRequestDto, String requestUrl, String loginId);

    T payCart(String cookieValue, String requestURL, String loginId);

    T completePayment(String payToken, String loginId);

    T cancelPayment(PayCancelRequestDto payCancelRequestDto);
}
