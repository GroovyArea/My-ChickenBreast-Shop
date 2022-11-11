package com.daniel.mychickenbreastshop.domain.product.item.model.dto.response;

import com.daniel.mychickenbreastshop.domain.product.category.model.enums.ChickenCategory;
import com.daniel.mychickenbreastshop.domain.product.item.model.enums.ChickenStatus;
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
