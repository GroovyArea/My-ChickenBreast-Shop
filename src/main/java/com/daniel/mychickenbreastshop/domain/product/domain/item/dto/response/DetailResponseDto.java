package com.daniel.mychickenbreastshop.domain.product.domain.item.dto.response;

import com.daniel.mychickenbreastshop.domain.product.domain.category.Category;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class DetailResponseDto {
    
    private Integer id;
    private String name;
    @Setter
    private Category category;
    private Integer price;
    private Integer quantity;
    private String content;
    @Setter
    private String image;
}
