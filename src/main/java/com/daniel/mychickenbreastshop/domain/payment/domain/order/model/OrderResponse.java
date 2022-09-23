package com.daniel.mychickenbreastshop.domain.payment.domain.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderResponse {

    ORDER_NOT_EXISTS("해당 주문 건이 존재하지 않습니다.");

    private final String message;
}
