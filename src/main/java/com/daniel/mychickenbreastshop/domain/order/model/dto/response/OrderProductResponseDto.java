package com.daniel.mychickenbreastshop.domain.order.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderProductResponseDto {

    private Long orderProductId;
    private Integer count;
    private String name;
    private Integer price;
    private String image;
    private String content;
}
