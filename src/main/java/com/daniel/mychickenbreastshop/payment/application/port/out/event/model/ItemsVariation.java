package com.daniel.mychickenbreastshop.payment.application.port.out.event.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemsVariation implements PaymentVariation {

    private List<Long> numbers;
    private List<Integer> quantities;
    private long totalAmount;
    private boolean status;
}
