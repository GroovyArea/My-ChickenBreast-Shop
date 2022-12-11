package com.daniel.mychickenbreastshop.order.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderProductResponseDto {

    private long orderProductId;
    private int count;
    private String name;
    private int price;
    private String image;
    private String content;
}
