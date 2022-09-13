package com.daniel.mychickenbreastshop.domain.payment.domain.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderedProductResponseDto {

    private Integer count;
    private String productName;
    private Integer productPrice;
    private String productImage;
    private String content;
}
