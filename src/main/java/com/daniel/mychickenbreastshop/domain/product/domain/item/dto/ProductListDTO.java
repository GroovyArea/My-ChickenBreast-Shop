package com.daniel.mychickenbreastshop.domain.product.domain.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ProductListDTO {

    private Integer productNo;
    private String productName;
    private Integer productPrice;
    private Integer productStock;
    private String productImage;

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
}
