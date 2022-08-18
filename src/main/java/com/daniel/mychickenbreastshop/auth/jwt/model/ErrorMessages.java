package com.daniel.mychickenbreastshop.auth.jwt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessages {
    TOKEN_MISMATCH("토큰이 일치하지 않습니다.");

    private final String message;
}
