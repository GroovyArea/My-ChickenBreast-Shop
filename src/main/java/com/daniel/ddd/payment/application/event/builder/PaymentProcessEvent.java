package com.daniel.ddd.payment.application.event.builder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentProcessEvent {

    private String eventType;
    private String payload;
    private String eventAction;
}
