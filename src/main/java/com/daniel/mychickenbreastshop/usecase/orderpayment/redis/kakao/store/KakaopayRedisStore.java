package com.daniel.mychickenbreastshop.usecase.orderpayment.redis.kakao.store;

import com.daniel.mychickenbreastshop.global.redis.store.RedisStore;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class KakaopayRedisStore extends RedisStore {

    public KakaopayRedisStore(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        super(redisTemplate, objectMapper);
    }

}
