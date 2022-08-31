package com.daniel.mychickenbreastshop.domain.cart.domain.dto.response;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class CartResponseDto {

    private Long productNo;
    private String productName;
    private int productQuantity;
    private long productPrice;

}
