package com.daniel.mychickenbreastshop.domain.product.domain.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum ChickenStatus {

    EXTINCTION(0), // 단종
    SALE(1), // 판매중
    SOLD_OUT(2); // 품절

    private final int statusNumber;

    public static ChickenStatus of(int status) {
        return Arrays.stream(ChickenStatus.values())
                .filter(chickenStatus -> chickenStatus.getStatusNumber() == status)
                .findAny()
                .orElse(SALE);
    }

}
