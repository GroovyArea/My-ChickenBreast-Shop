package com.daniel.mychickenbreastshop.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Builder
public class JoinRequestDto {

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

}
