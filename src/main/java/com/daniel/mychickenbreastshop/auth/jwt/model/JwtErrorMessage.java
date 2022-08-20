package com.daniel.mychickenbreastshop.auth.jwt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtErrorMessage {
    MALFORMED("유효하지 않는 토큰입니다."),
    EXPIRED("토큰 유효기간이 만료되었습니다. 재로그인 요망."),
    UNSUPPORTED("지원하지 않는 토큰입니다."),
    CLASS_CAST_FAIL("토큰의 유효성 검사가 실패했습니다. 확인 후 재요청 요망."),
    INVALID_SIGNATURE("토큰 형식이 잘못되었습니다.");

    private final String message;
}
