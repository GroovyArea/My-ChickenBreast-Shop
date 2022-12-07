package com.daniel.ddd.payment.application.event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderVariation implements PaymentEvent{

    private Long paymentId;
    private boolean status;
}
