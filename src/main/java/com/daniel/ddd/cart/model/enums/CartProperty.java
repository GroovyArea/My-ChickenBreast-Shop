package com.daniel.ddd.cart.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CartProperty {

    COOKIE_KEY("chicken");

    private final String key;
}
