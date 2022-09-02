package com.daniel.mychickenbreastshop.domain.order.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderStatus {

    ORDER_APPROVAL("결제 승인"),
    CANCEL_ORDER("결제 취소");

    private final String status;
}
