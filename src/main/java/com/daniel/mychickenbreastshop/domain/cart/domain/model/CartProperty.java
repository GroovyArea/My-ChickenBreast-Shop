package com.daniel.mychickenbreastshop.domain.cart.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CartProperty {

    COOKIE_KEY("chicken");

    private final String key;
}
