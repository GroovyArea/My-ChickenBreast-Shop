package com.daniel.mychickenbreastshop.domain.user.dto.response;

import com.daniel.mychickenbreastshop.domain.user.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class DetailDto {

    private Long userId;
    private String loginId;
    private String name;
    private String email;
    private String address;
    private String zipcode;
    private Role role;
}
