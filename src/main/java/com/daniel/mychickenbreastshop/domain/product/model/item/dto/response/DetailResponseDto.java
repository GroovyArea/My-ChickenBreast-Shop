package com.daniel.mychickenbreastshop.domain.product.model.item.dto.response;

import com.daniel.mychickenbreastshop.domain.product.model.category.enums.ChickenCategory;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class DetailResponseDto {
    
    private long id;
    private String name;
    private ChickenCategory categoryName;
    private int price;
    private int quantity;
    private String content;
    private String image;

    public void updateImageUrl(String image) {
        this.image = image;
    }
}
