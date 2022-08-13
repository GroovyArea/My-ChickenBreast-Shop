package com.daniel.mychickenbreastshop.domain.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum RoleType {

    WITHDRAWAL_USER(0),
    USER(1),
    ADMIN(9);

    private final int grade;

    public static Optional<RoleType> of(int gradeNumber) {
        return Optional.of(Arrays.stream(RoleType.values())
                .filter(userGrade -> userGrade.getGrade() == gradeNumber)
                .findFirst()
                .orElse(USER));
    }

}
