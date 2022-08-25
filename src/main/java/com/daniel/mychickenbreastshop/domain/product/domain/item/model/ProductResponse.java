package com.daniel.mychickenbreastshop.domain.product.domain.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductResponse {

    ITEM_NOT_EXISTS("상품이 존재하지 않습니다.");


    private final String message;
}
