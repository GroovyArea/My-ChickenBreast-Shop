package com.daniel.mychickenbreastshop.product.item.model.dto.response;


import com.daniel.mychickenbreastshop.product.category.domain.enums.ChickenCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
