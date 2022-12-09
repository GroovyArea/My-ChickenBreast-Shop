package com.daniel.mychickenbreastshop.cart.application.port.out.event.model;

import com.daniel.mychickenbreastshop.global.event.model.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartValidation implements DomainEvent {

    private long itemNo;
    private int itemQuantity;
    private long totalPrice;

}
