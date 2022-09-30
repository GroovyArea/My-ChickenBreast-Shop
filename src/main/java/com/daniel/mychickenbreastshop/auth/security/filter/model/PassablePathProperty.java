package com.daniel.mychickenbreastshop.auth.security.filter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PassablePathProperty {

    JOIN("join/"),
    EMAIL("join/email"),
    TEST("/api/test");

    private final String path;
}
