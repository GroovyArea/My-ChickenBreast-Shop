package com.daniel.mychickenbreastshop.auth.jwt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtProperties {

    ID("id"),
    LOGIN_ID("loginId"),
    ROLE("role"),
    AUTH_TYPE("Bearer "),
    TOKEN_HEADER_KEY("Authorization");

    private final String key;
}
