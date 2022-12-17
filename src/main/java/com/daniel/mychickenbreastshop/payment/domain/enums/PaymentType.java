package com.daniel.mychickenbreastshop.payment.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PaymentType {

    CASH("현금 결제"),
    CARD("카드 결제");

    private final String type;
}
