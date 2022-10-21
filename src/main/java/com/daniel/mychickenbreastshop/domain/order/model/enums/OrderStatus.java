package com.daniel.mychickenbreastshop.domain.order.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderStatus {

    ORDER_READY("주문 준비"),
    ORDER_COMPLETE("주문 완료"),
    CANCEL_ORDER("주문 취소");

    private final String statusName;
}
