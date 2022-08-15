package com.daniel.mychickenbreastshop.domain.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Role {

    ROLE_WITHDRAWAL(0, "WITHDRAWAL"),
    ROLE_USER(1, "USER"),
    ROLE_ADMIN(9, "ADMIN");

    private final int roleNumber;
    private final String roleName;

    public static Role ofInt(int gradeNumber) {
        return Arrays.stream(Role.values())
                .filter(role -> role.getRoleNumber() == gradeNumber)
                .findFirst()
                .orElse(ROLE_USER);
    }

    public static Role ofString(String gradeName) {
        return Arrays.stream(Role.values())
                .filter(role -> role.getRoleName().equals(gradeName))
                .findFirst()
                .orElse(ROLE_USER);
    }

}
