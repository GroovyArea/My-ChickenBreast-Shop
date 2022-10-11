package com.daniel.mychickenbreastshop.domain.product.model.item.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("product")
@Getter
@AllArgsConstructor
public class RedisProduct {

    @Id
    private String id;
    private Integer quantity;
}
