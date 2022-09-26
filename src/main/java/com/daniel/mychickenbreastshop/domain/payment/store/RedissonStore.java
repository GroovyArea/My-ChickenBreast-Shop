package com.daniel.mychickenbreastshop.domain.payment.store;

import com.daniel.mychickenbreastshop.global.store.KeyValueStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedissonStore implements KeyValueStore {

    private static final String LOCK_SUFFIX = ":lock";
    private static final long WAIT_TIME_SECONDS = 3L;
    private static final long LEASE_TIME_SECONDS = 3L;

    private final RedissonClient redissonClient;
    private final PlatformTransactionManager transactionManager;

    @Override
    public Optional<Object> getValue(String key) {
        RBucket<Object> bucket = redissonClient.getBucket(key);

        if (bucket.isExists()) {
            return Optional.of(bucket.get());
        }

        return Optional.empty();
    }

    @Override
    public <T> void save(String key, T value) {
        redissonClient.getBucket(key).set(value);
    }

    @Override
    public <T> T executeWithLock(String key, Supplier<T> supplier) throws InterruptedException {
        RLock lock = redissonClient.getLock(key + LOCK_SUFFIX);

        TransactionStatus transaction = getTransaction();

        if (lock.tryLock(WAIT_TIME_SECONDS, LEASE_TIME_SECONDS, TimeUnit.SECONDS)) {
            try {
                supplier.get();
            } catch (Exception e) {
                transactionManager.rollback(transaction);
                throw e;
            } finally {
                transactionManager.commit(transaction);
                unlock(lock);
                log.info("Redis unlock!");
            }
        }
        return supplier.get();
    }

    private TransactionStatus getTransaction() {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        return transactionManager.getTransaction(transactionDefinition);
    }

    private void unlock(RLock lock) {
        if (lock != null && lock.isLocked()) {
            lock.unlock();
        }
    }
}
