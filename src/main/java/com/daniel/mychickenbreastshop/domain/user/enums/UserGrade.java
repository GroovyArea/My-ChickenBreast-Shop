package com.daniel.mychickenbreastshop.domain.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum UserGrade {

    WITHDRAWAL_USER(0),
    BASIC_USER(1),
    ADMIN(9);

    private final int grade;

    public static Optional<UserGrade> of(int gradeNumber) {
        return Optional.of(Arrays.stream(UserGrade.values())
                .filter(userGrade -> userGrade.getGrade() == gradeNumber)
                .findFirst()
                .orElse(BASIC_USER));
    }

}
