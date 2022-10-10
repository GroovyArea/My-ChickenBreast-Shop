package com.daniel.mychickenbreastshop.global.redis.store;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Redis Component 클래스
 *
 * <pre>
 *     <History>
 *         1.0 2022.05 최초 작성
 *         1.1 2022.08.01 리팩토링
 *     </History>
 *
 * </pre>
 *
 * @author 김남영
 * @version 1.1
 */

// 생각
@Component
@RequiredArgsConstructor
public abstract class RedisStore {

    private final RedisTemplate<String, Object> stringRedisTemplate;

    /* key를 통해 value 리턴 */
    public String getData(String key) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    /* 데이터 삽입 */
    public void setData(String key, String value) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    /* 유효 시간 동안 (key, value) 저장 */
    public void setDataExpire(String key, String value, long duration) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
    }

    /* 데이터 삭제 */
    public Boolean deleteData(String key) {
        return stringRedisTemplate.delete(key);
    }

}
