package com.daniel.mychickenbreastshop.domain.user.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public void changeNameWithUserRole(String roleName) {
        role = roleName;
    }
}
