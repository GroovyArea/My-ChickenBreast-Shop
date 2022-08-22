package com.daniel.mychickenbreastshop.domain.user.dto;

import com.daniel.mychickenbreastshop.domain.user.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserDTO {

    private String loginId;
    private String userName;
    private String userEmail;
    private String userAddress;
    private String userZipcode;
    private Role roleType;
}
