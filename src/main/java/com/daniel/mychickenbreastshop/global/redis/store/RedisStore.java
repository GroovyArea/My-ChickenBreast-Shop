package com.daniel.mychickenbreastshop.global.redis.store;

import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import com.daniel.mychickenbreastshop.global.error.exception.InternalErrorException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

/**
 * Redis Component 클래스
 *
 * <pre>
 *     <History>
 *         1.0 2022.05 최초 작성
 *         1.1 2022.08.01 리팩토링
 *         1.2 2022.10.11 확장을 위해 인터페이스로 변경
 *         1.3 2022.10.12 확장을 위한 추상클래스로 변경
 *     </History>
 *
 * </pre>
 *
 * @author 김남영
 * @version 1.3
 */

@RequiredArgsConstructor
public abstract class RedisStore {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    /* key를 통해 value 리턴 */
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
        try {
            String jsonValue = objectMapper.writeValueAsString(value);
            ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
            valueOperations.set(key, jsonValue);
        } catch (JsonProcessingException e) {
            throw new InternalErrorException(e);
        }
    }

    public void setDataExpire(String key, Object value, long duration) {
        try {
            String jsonValue = objectMapper.writeValueAsString(value);
            ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
            Duration expireDuration = Duration.ofSeconds(duration);
            valueOperations.set(key, jsonValue, expireDuration);
        } catch (JsonProcessingException e) {
            throw new InternalErrorException(e);
        }
    }

    public Boolean deleteData(String key) {
        return redisTemplate.delete(key);
    }

}
