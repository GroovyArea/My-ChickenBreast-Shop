package com.daniel.ddd.global.redis.function;

import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

public interface RedissonFunctionProvider {

    RLock lock(String lockKey);

    RLock lock(String lockKey, long timeout);

    RLock lock(String lockKey, TimeUnit unit, long timeout);

    boolean tryLock(String lockKey, TimeUnit unit, long waitTime, long leaseTime);

    void unlock(String lockKey);

    void unlock(RLock lock);

    Object getValue(String key);

    void setValue(String key, Object value);

    boolean canUnlock(String lockKey);
}



