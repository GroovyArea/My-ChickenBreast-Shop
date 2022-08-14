package com.daniel.mychickenbreastshop.domain.user.dto;

import com.daniel.mychickenbreastshop.domain.user.domain.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserDTO {

    private String userId;
    private String userPw;
    private String userSalt;
    private String userName;
    private String userPhone;
    private String userEmail;
    private String userMainAddress;
    private String userDetailAddress;
    private String userZipcode;
    private RoleType roleType;
}
