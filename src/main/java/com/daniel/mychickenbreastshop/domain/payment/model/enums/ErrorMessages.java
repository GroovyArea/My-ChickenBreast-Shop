package com.daniel.mychickenbreastshop.domain.payment.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessages {

    CANCELED_PAY("결제 도중 취소하셨습니다."),
    FAILED_PAY("결제에 실패하였습니다."),
    UNCORRECTED_API("해당 결제 API를 이용할 수 없습니다."),
    PAYMENT_NOT_EXISTS("해당 조건에 맞는 결제 건이 존재하지 않습니다.");

    private final String message;
}
