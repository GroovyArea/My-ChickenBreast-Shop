package com.daniel.ddd.cart.model.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponseDto {

    private Long itemNo;
    private String itemName;
    private Integer itemQuantity;
    private Long totalPrice;

}
