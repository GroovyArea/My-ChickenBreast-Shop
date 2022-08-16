package com.daniel.mychickenbreastshop.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginRequestDto {

    private final String loginId;
    private final String password;
}
