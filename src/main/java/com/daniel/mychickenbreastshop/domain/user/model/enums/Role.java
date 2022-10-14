package com.daniel.mychickenbreastshop.domain.user.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Role {

    ROLE_WITHDRAWAL(0, "탈퇴회원"),
    ROLE_USER(1, "일반회원"),
    ROLE_ADMIN(9, "관리자");

    private final int roleNumber;
    private final String roleName;

    public static Role ofNumber(int gradeNumber) {
        return Arrays.stream(Role.values())
                .filter(role -> role.getRoleNumber() == gradeNumber)
                .findFirst()
                .orElse(ROLE_USER);
    }

    public static Role ofName(String gradeName) {
        return Arrays.stream(Role.values())
                .filter(role -> role.getRoleName().equals(gradeName))
                .findFirst()
                .orElse(ROLE_USER);
    }

}
