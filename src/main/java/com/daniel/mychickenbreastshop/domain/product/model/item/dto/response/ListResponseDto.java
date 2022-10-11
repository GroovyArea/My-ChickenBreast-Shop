package com.daniel.mychickenbreastshop.domain.product.model.item.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private String category;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public void changeStatusNameWithChickenStatus(String statusName) {
        status = statusName;
    }

    public void changeCategoryNameWithChickenCategory(String categoryName) {
        category = categoryName;
    }

}
