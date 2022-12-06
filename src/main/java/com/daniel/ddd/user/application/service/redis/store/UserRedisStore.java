package com.daniel.ddd.user.application.service.redis.store;

import com.daniel.mychickenbreastshop.global.redis.store.RedisStore;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserRedisStore extends RedisStore {

    public UserRedisStore(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        super(redisTemplate, objectMapper);
    }

}