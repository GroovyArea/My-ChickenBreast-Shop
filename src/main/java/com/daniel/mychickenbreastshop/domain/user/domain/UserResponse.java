package com.daniel.mychickenbreastshop.domain.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserResponse {

    USER_EXISTS("User already exists."),
    USER_NOT_EXISTS("User not exists."),
    WITHDRAW_USER("Retired member."),
    WRONG_PW("Passwords do not match."),
    JOIN_SUCCEED("Join succeed."),
    LOGIN_SUCCEED("Login succeed."),
    MAIL_SEND("Email is sent");

    private final String message;
}
