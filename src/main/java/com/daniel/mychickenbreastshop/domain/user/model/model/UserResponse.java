package com.daniel.mychickenbreastshop.domain.user.model.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserResponse {

    USER_EXISTS("이미 동일 회원 정보가 존재합니다."),
    USER_NOT_EXISTS("해당 회원 정보가 존재하지 않습니다."),
    WITHDRAW_USER("탈퇴 회원입니다."),
    MAIL_SEND("이메일 인증번호가 전송 되었습니다."),
    MAIL_KEY_EXPIRED("인증 번호가 만료되었습니다."),
    MAIL_KEY_MISMATCH("인증 번호가 일치하지 않습니다. 재인증 받아 주세요.");

    private final String message;
}
