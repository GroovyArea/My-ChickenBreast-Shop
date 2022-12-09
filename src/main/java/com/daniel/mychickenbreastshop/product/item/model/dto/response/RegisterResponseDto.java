package com.daniel.mychickenbreastshop.product.item.model.dto.response;

import com.daniel.mychickenbreastshop.product.category.domain.enums.ChickenCategory;
import com.daniel.mychickenbreastshop.product.item.domain.enums.ChickenStatus;
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
    private String image;
    private ChickenStatus status;
    private ChickenCategory category;

    public void updateImage(final String imageURL) {
        this.image = imageURL;
    }
}
