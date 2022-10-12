package com.daniel.mychickenbreastshop.domain.product.model.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductResponse {

    ITEM_NOT_EXISTS("상품이 존재하지 않습니다."),
    INVALID_PAY_AMOUNT("상품 총 가격이 잘못 되었습니다."),
    ITEM_QUANTITY_NOT_ENOUGH("상품 재고 수량이 불충분합니다. ");

    private final String message;
}
