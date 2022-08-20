package com.daniel.mychickenbreastshop.auth.security.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SecurityMessage {

    PASSWORD_MISMATCH("비밀번호가 일치하지 않습니다."),
    HEADER_LESS("Authorization 헤더가 없습니다.");


    private final String message;
}
