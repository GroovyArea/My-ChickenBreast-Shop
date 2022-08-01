package com.daniel.mychickenbreastshop.domain.cart.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDTO {

    private int productNo;
    private String productName;
    private int productStock;
    private int productPrice;

}
