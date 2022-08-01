package com.daniel.mychickenbreastshop.domain.cart.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartRequestDTO {

    private int productNo;
    private String productName;
    private int productStock;
    private int productPrice;

}
