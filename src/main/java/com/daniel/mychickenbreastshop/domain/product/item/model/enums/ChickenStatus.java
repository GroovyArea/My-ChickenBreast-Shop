package com.daniel.mychickenbreastshop.domain.product.item.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum ChickenStatus {

    EXTINCTION(0, "단종 상품"),
    SALE(1, "판매 상품"),
    SOLD_OUT(2, "품절 상품");

    private final int statusNumber;
    private final String statusName;

    public static ChickenStatus of(int status) {
        return Arrays.stream(ChickenStatus.values())
                .filter(chickenStatus -> chickenStatus.getStatusNumber() == status)
                .findAny()
                .orElse(SALE);
    }

}
