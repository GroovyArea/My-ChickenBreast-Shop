package com.daniel.mychickenbreastshop.user.auth.security.filter.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PassablePathProperty {

    JOIN("join/"),
    EMAIL("join/email");

    private final String path;
}
