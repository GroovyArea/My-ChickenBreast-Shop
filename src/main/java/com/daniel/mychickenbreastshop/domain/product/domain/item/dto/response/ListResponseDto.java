package com.daniel.mychickenbreastshop.domain.product.domain.item.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ListResponseDto {

    private Long id;
    private String name;
    private Integer price;
    private Integer quantity;
    private String image;
}
