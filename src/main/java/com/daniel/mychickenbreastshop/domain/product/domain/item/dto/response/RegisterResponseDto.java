package com.daniel.mychickenbreastshop.domain.product.domain.item.dto.response;

import com.daniel.mychickenbreastshop.domain.product.domain.category.ChickenCategory;
import com.daniel.mychickenbreastshop.domain.product.domain.item.model.ChickenStatus;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RegisterResponseDto {

    private Long id;
    private String name;
    private Integer price;
    private Integer quantity;
    private String content;
    @Setter
    private String image;
    private ChickenStatus status;
    private ChickenCategory category;
}
