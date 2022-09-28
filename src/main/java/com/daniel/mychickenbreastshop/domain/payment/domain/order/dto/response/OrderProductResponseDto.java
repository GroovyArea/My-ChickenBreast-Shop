package com.daniel.mychickenbreastshop.domain.payment.domain.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductResponseDto {

    private Long orderProductId;
    private Integer count;
    private String name;
    private Integer price;
    private String image;
    private String content;
}
