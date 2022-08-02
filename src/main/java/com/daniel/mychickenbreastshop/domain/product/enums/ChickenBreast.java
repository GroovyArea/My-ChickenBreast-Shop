package com.daniel.mychickenbreastshop.domain.product.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum ChickenBreast {

    STEAMED(1), // 스팀
    SMOKED(2), // 훈제
    SAUSAGE(3), // 소시지
    STEAK(4), // 스테이크
    BALL(5), // 볼
    RAW(6); // 생닭가슴살

    private final int chickenNumber;

    public static ChickenBreast of(int chickenNumber) {
        return Arrays.stream(ChickenBreast.values())
                .filter(number -> number.getChickenNumber() == chickenNumber)
                .findAny()
                .orElse(STEAMED);
    }

}
