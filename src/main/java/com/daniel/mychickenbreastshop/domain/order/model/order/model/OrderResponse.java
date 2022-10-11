package com.daniel.mychickenbreastshop.domain.payment.model.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderResponse {

    ORDER_NOT_EXISTS("해당 주문 건이 존재하지 않습니다."),
    ORDER_QUANTITY_NOT_ENOUGH("주문 상품 수량 재고가 모자랍니다.");

    private final String message;
}
