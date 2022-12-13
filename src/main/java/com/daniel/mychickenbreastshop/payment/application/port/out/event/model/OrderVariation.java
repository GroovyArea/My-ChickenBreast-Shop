package com.daniel.mychickenbreastshop.payment.application.port.out.event.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderVariation implements PaymentVariation {

    private Long orderId;
    private Long paymentId;
    private boolean status;
}
