package com.daniel.mychickenbreastshop.global.auth.enums;

public enum FilterMessage {

    NULL_TOKEN("DB에 토큰이 존재하지 않습니다. 로그인이 필요합니다."),
    INVALID_TOKEN("토큰이 일치하지 않습니다. 잘못된 접근입니다."),
    INVALID_SIGNATURE("토큰 형식이 잘못 되었습니다."),
    CLASS_CAST_FAIL("토큰 유효성 검사가 실패하였습니다. 확인 후 재요청 바랍니다."),
    MALFORMED_TOKEN("유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN("토큰 유효기간이 만료되었습니다. 재로그인 바랍니다."),
    UNSUPPORTED_TOKEN("지원하지 않는 토큰입니다.");

    private final String message;

    FilterMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
