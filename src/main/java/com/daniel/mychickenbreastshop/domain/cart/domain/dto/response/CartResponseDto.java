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
    private Integer itemQuantity;
    private Long totalPrice;

}
