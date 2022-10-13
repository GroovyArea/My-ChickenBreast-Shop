package com.daniel.mychickenbreastshop.usecase.orderpayment.extract.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItem {

    private Long itemNo;
    private String itemName;
    private Integer itemQuantity;
    private Long totalPrice;
}
