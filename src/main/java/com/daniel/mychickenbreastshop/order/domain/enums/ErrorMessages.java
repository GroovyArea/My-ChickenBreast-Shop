package com.daniel.mychickenbreastshop.order.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessages {

    ORDER_NOT_EXISTS("해당 주문 건이 존재하지 않습니다."),
    ORDER_QUANTITY_NOT_ENOUGH("주문 상품 수량 재고가 모자랍니다."),
    CANNOT_CANCEL_ORDER("이미 결제된 주문 건에 대해 취소가 불가능합니다.");

    private final String message;
}
