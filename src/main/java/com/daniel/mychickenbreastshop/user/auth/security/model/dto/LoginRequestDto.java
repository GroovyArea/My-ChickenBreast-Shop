package com.daniel.mychickenbreastshop.user.auth.security.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

    private String loginId;
    private String password;
}
