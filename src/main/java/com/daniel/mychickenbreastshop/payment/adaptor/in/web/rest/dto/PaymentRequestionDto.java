package com.daniel.mychickenbreastshop.payment.adaptor.in.web.rest.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PaymentRequestionDto {

    private String redirectURL;
    private Long paymentId;
}
