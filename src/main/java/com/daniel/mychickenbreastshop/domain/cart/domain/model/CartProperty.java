package com.daniel.mychickenbreastshop.domain.cart.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CartProperty {

    COOKIE_KEY("Chicken");

    private final String key;
}
