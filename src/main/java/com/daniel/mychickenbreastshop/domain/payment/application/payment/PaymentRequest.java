package com.daniel.mychickenbreastshop.domain.payment.application.payment;

import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.request.ItemPayRequestDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.request.PayCancelRequestDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.model.PaymentApi;

public interface PaymentRequest {

    PaymentApi getPaymentApiName();

    <T> T getOrderInfo(String franchiseeId, String payId, Class<T> clazz);

    <T> T payItem(ItemPayRequestDto itemPayRequestDto, String requestURL, String loginId, Class<T> clazz);

    <T> T payCart(String cookieValue, String requestURL, String loginId, Class<T> clazz);

    <T> T completePayment(String payToken, String loginId, Class<T> clazz);

    <T> T cancelPayment(PayCancelRequestDto payCancelRequestDto, Class<T> clazz);

}
