package com.daniel.ddd.payment.application.service.strategy;

import com.daniel.ddd.payment.application.service.gateway.model.PaymentResult;
import com.daniel.ddd.payment.model.dto.request.ItemPayRequestDto;
import com.daniel.ddd.payment.model.dto.request.PayCancelRequestDto;
import com.daniel.ddd.payment.model.enums.PaymentGateway;

import java.util.List;

/**
 * 결제 API 서비스 전략
 */
public interface PaymentStrategyService<T extends PaymentResult> {

    PaymentGateway getPaymentGatewayName();

    T getOrderInfo(String franchiseeId, String payId, String requestUrl);

    T payItems(List<ItemPayRequestDto> itemPayRequestDtos, String requestUrl, String loginId, Long orderId);

    T completePayment(String payToken, Long paymentId, String loginId);

    T cancelPayment(PayCancelRequestDto payCancelRequestDto, Long paymentId, String loginId);
}
