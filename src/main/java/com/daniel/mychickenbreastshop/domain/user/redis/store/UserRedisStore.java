package com.daniel.mychickenbreastshop.domain.user.redis.store;

import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import com.daniel.mychickenbreastshop.global.error.exception.InternalErrorException;
import com.daniel.mychickenbreastshop.global.redis.store.RedisStore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class UserRedisStore implements RedisStore {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public <T> T getData(String key, Class<T> type) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        String value = (String) valueOperations.get(key);

        if (StringUtils.isBlank(value)) {
            throw new BadRequestException("해당 키에 맞는 값이 존재하지 않습니다.");
        }

        try {
            return objectMapper.readValue(value, type);
        } catch (JsonProcessingException e) {
            throw new InternalErrorException(e);
        }

    }

    public void setData(String key, Object value) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    public void setDataExpire(String key, Object value, long duration) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
    }

    public Boolean deleteData(String key) {
        return redisTemplate.delete(key);
    }

}