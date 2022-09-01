package com.daniel.mychickenbreastshop.domain.product.domain.item.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class DetailResponseDto {
    
    private Long id;
    private String name;
    private String categoryName;
    private Integer price;
    private Integer quantity;
    private String content;
    @Setter
    private String image;
}