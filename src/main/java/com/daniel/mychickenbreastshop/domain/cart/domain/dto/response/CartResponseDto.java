package com.daniel.mychickenbreastshop.domain.cart.domain.dto.response;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class CartResponseDto {

    private Long itemNo;
    private String itemName;
    private int itemQuantity;
    private long totalPrice;

}
