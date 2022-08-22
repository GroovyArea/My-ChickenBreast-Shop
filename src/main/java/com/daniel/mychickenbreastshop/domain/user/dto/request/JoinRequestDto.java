package com.daniel.mychickenbreastshop.domain.user.dto.request;

import com.daniel.mychickenbreastshop.domain.user.domain.Role;
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
    @Setter
    private Role roleType;
    private String emailAuthKey;

}
