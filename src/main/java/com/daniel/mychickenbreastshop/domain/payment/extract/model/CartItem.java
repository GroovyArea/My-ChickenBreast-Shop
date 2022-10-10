package com.daniel.mychickenbreastshop.domain.payment.extract.model;

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
