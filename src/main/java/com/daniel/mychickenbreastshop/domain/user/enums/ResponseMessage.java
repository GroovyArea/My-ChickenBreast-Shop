package com.daniel.mychickenbreastshop.domain.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseMessage {

    USER_EXISTS_MESSAGE("User already exists."),
    LOGIN_FAIL_MESSAGE("Member information for the ID does not exists."),
    WITHDRAW_USER_MESSAGE("Retired member."),
    WRONG_PW_MESSAGE("Passwords do not match."),
    JOIN_SUCCEED_MESSAGE("Join succeed.");

    private final String message;
}
