package com.daniel.mychickenbreastshop.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserLoginDTO {

    private final String loginId;
    private final String password;
}
