package com.daniel.mychickenbreastshop.domain.cart.model.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CartProperty {

    COOKIE_KEY("chicken");

    private final String key;
}
