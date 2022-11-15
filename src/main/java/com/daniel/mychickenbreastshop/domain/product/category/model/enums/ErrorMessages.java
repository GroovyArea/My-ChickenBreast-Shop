package com.daniel.mychickenbreastshop.domain.product.category.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessages {

    CATEGORY_NOT_EXISTS("해당 카테고리가 존재하지 않습니다.");

    private final String message;
}
