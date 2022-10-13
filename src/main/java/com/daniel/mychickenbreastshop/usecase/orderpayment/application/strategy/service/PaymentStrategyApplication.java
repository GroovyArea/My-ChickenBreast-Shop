package com.daniel.mychickenbreastshop.usecase.orderpayment.application.strategy.service;

import com.daniel.mychickenbreastshop.usecase.orderpayment.application.strategy.model.PaymentResult;
import com.daniel.mychickenbreastshop.usecase.orderpayment.model.dto.request.ItemPayRequestDto;
import com.daniel.mychickenbreastshop.usecase.orderpayment.model.dto.request.PayCancelRequestDto;
import com.daniel.mychickenbreastshop.usecase.orderpayment.model.enums.PaymentGateway;

/**
 * 결제 API 서비스 전략
 */
public interface PaymentStrategyApplication <T extends PaymentResult> {

    PaymentGateway getPaymentGatewayName();

    T payItem(ItemPayRequestDto itemPayRequestDto, String requestUrl, String loginId);

    T payCart(String cookieValue, String requestURL, String loginId);

    T completePayment(String payToken, String loginId);

    T cancelPayment(PayCancelRequestDto payCancelRequestDto, String loginId);
}
