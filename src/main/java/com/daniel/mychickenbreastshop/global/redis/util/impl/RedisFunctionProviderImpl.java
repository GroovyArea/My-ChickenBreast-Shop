package com.daniel.mychickenbreastshop.global.redis.util.impl;

import com.daniel.mychickenbreastshop.global.redis.util.RedisFunctionProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RTransaction;
import org.redisson.api.RedissonClient;
import org.redisson.api.TransactionOptions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.concurrent.TimeUnit;

@Component
@Getter
@RequiredArgsConstructor
public class RedisFunctionProviderImpl implements RedisFunctionProvider {

    private final RedissonClient redissonClient;
    private final PlatformTransactionManager transactionManager;

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
    public TransactionStatus startDBTransacton() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        return transactionManager.getTransaction(def);
    }

    @Override
    public void commitDB(TransactionStatus status) {
        transactionManager.commit(status);
    }

    @Override
    public void rollbackDB(TransactionStatus status) {
        transactionManager.rollback(status);
    }

    @Override
    public RTransaction startRedisTransacton() {
        return redissonClient.createTransaction(TransactionOptions.defaults());
    }

    @Override
    public void commitRedis(RTransaction transaction) {
        transaction.commit();
    }

    @Override
    public void rollbackRedis(RTransaction transaction) {
        transaction.rollback();
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
