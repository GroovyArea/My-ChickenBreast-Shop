package com.daniel.mychickenbreastshop.domain.cart.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessages {

    CART_COOKIE_NOT_EXISTS("장바구니 쿠키가 없습니다."),
    MODIFIABLE_COOKIE_NOT_EXISTS("변경하려는 장바구니 쿠키가 없습니다."),
    REMOVABLE_COOKIE_NOT_EXISTS("삭제하려는 장바구니 쿠키가 없습니다.");

    private final String message;
}
