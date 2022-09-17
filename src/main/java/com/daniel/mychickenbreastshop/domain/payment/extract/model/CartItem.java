package com.daniel.mychickenbreastshop.domain.payment.extract.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {

    private Long itemNo;
    private String itemName;
    private Integer itemQuantity;
    private Long totalPrice;
}
