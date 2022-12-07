package com.daniel.ddd.user.model.dto.response;

import com.daniel.mychickenbreastshop.domain.user.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class DetailResponseDto {

    private Long userId;
    private String loginId;
    private String name;
    private String email;
    private String address;
    private String zipcode;
    private Role role;
}
