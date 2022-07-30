package com.daniel.mychickenbreastshop.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class UserLoginDTO {

    private final String userId;
    private final String userPw;
}
