package com.daniel.mychickenbreastshop.domain.user.dto.request;

import com.daniel.mychickenbreastshop.domain.user.domain.RoleType;
import com.daniel.mychickenbreastshop.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;

@Getter
@AllArgsConstructor
@Builder
public class UserJoinDto {

    private String loginId;
    @Setter
    private String password;
    @Setter
    private String salt;
    private String name;
    private String email;
    private String address;
    private String zipcode;
    private String emailAuthKey;

    public User toEntity() {
        return User.builder()
                .loginId(loginId)
                .password(password)
                .salt(salt)
                .name(name)
                .email(email)
                .address(address)
                .zipcode(zipcode)
                .roles(Collections.singletonList(RoleType.USER.name()))
                .build();
    }
}
