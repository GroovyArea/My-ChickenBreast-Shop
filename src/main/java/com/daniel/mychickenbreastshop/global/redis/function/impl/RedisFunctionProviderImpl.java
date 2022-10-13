package com.daniel.mychickenbreastshop.global.redis.function.impl;

import com.daniel.mychickenbreastshop.global.redis.function.RedisFunctionProvider;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisFunctionProviderImpl implements RedisFunctionProvider {

    private final RedissonClient redissonClient;

    @Override
    public RLock lock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        return lock;
    }

    @Override
    public RLock lock(String lockKey, long leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(leaseTime, TimeUnit.SECONDS);
        return lock;
    }

    @Override
    public RLock lock(String lockKey, TimeUnit unit, long timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, unit);
        return lock;
    }

    @Override
    public boolean tryLock(String lockKey, TimeUnit unit, long waitTime, long leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.unlock();
    }

    @Override
    public void unlock(RLock lock) {
        lock.unlock();
    }

    @Override
    public Object getValue(String key)
    {
        return redissonClient.getBucket(key).get();
    }

    @Override
    public void setValue(String key, Object value)
    {
        redissonClient.getBucket(key).set(value);
    }

    @Override
    public boolean canUnlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        return lock != null && lock.isLocked() && lock.isHeldByCurrentThread();
    }
}
