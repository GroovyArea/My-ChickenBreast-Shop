package com.daniel.mychickenbreastshop.domain.product.model.category.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryResponse {

    CATEGORY_NOT_EXISTS("해당 카테고리가 존재하지 않습니다.");

    private final String message;
}
