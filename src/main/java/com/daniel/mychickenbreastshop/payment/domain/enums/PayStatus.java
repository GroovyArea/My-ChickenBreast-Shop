package com.daniel.mychickenbreastshop.payment.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PayStatus {

    READY("결제 준비"),
    COMPLETED("결제 완료"),
    CANCELED("결제 취소");

    private final String status;
}
