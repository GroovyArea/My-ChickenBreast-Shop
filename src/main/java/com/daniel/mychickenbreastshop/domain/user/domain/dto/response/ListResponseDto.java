package com.daniel.mychickenbreastshop.domain.user.domain.dto.response;

import com.daniel.mychickenbreastshop.domain.user.domain.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ListResponseDto {

    private String userId;
    private String loginId;
    private String name;
    private String email;
    private String address;
    private String zipcode;
    private Role role;
}
