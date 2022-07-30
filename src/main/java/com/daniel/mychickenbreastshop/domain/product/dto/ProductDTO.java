package com.daniel.mychickenbreastshop.domain.product.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductDTO {
    
    private Integer productNo;
    private String productName;
    private Integer productCategory;
    private Integer productPrice;
    private Integer productStock;
    private String productDetail;
    private String productImage;
    private Integer productStatus;
}
