package com.daniel.ddd.payment.application.event.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemsVariation implements PaymentEvent{

    private List<Long> numbers;
    private List<Integer> quantities;
    private long totalAmount;
    private boolean status;
}
