package com.daniel.mychickenbreastshop.auth.security.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SecurityMessages {

    PASSWORD_MISMATCH("비밀번호가 일치하지 않습니다."),
    HEADER_LESS("Authorization 헤더가 없습니다."),
    PERMISSION_DENIED("잘못된 권한으로의 접근입니다.");

    private final String message;
}
