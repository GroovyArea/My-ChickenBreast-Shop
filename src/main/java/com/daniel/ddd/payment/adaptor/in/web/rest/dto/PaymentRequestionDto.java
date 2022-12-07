package com.daniel.ddd.payment.adaptor.in.web.rest.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PaymentRequestionDto {

    private String redirectURL;
    private Long paymentId;
}
