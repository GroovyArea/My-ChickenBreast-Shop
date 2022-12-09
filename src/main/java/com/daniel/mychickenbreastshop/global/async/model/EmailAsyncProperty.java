package com.daniel.mychickenbreastshop.global.async.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailAsyncProperty {

    CORE_POOL_SIZE(5),
    MAX_POOL_SIZE(10),
    QUEUE_CAPACITY(200);

    private final int size;

}
