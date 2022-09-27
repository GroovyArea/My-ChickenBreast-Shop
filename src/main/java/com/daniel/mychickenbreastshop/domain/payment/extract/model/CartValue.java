package com.daniel.mychickenbreastshop.domain.payment.extract.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartValue {

    private List<Long> itemNumbers;
    private List<String> itemNames;
    private List<Integer> itemQuantities;
    private long totalPrice;

}
