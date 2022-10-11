package com.daniel.mychickenbreastshop.domain.payment.application.strategy.service;

import com.daniel.mychickenbreastshop.domain.payment.application.strategy.model.PaymentResult;
import com.daniel.mychickenbreastshop.domain.payment.model.dto.request.ItemPayRequestDto;
import com.daniel.mychickenbreastshop.domain.payment.model.dto.request.PayCancelRequestDto;
import com.daniel.mychickenbreastshop.domain.payment.model.model.PaymentApi;

/**
 * 결제 API 서비스 전략
 */
public interface PaymentStrategyApplication <T extends PaymentResult> {

    PaymentApi getPaymentApiName();

    T payItem(ItemPayRequestDto itemPayRequestDto, String requestUrl, String loginId);

    T payCart(String cookieValue, String requestURL, String loginId);

    T completePayment(String payToken, String loginId);

    T cancelPayment(PayCancelRequestDto payCancelRequestDto, String loginId);
}
