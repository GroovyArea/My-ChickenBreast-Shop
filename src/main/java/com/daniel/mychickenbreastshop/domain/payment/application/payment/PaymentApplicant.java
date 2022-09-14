package com.daniel.mychickenbreastshop.domain.payment.application.payment;

import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.request.ItemPayRequestDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.request.PayCancelRequestDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.model.PaymentApi;

public interface PaymentApplicant {

    PaymentApi getPaymentApiName();

    String payItem(ItemPayRequestDto itemPayRequestDto, String requestURL, Long userId);

    String payCart(String cookieValue, String requestURL, Long userId);

    void completePayment(String payToken);

    void cancelPayment(PayCancelRequestDto payCancelRequestDto);

}
