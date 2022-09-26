package com.daniel.mychickenbreastshop.domain.payment.aspect;

import com.daniel.mychickenbreastshop.global.error.exception.InternalErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RedisLockAspect {

    private final RedissonClient redissonClient;

    private static final String LOCK_SUFFIX = ":lock";
    private static final long WAIT_TIME_SECONDS = 3L;
    private static final long LEASE_TIME_SECONDS = 3L;

    @Around("@annotation(com.daniel.mychickenbreastshop.domain.payment.aspect.annotation.RedisLocked)")
    public Optional<Object> executeWithLock(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String key = args[2] + "product" + "order"; // 좀 더 추상화 시켜볼 것. 다른 곳에서도 이용 가능하게

        RLock lock = redissonClient.getLock(key + LOCK_SUFFIX);

        try {
            if (!lock.tryLock(WAIT_TIME_SECONDS, LEASE_TIME_SECONDS, TimeUnit.SECONDS)) {
                throw new InternalErrorException("Lock 획득에 실패하였습니다.");
            }
            return Optional.of(joinPoint.proceed(args));
        } catch (Throwable e) {
            throw new InternalErrorException(e);
        } finally {
            unlock(lock);
            log.info("Redis unlocked!");
        }
    }

    private void unlock(RLock lock) {
        if (lock != null && lock.isLocked()) {
            lock.unlock();
        }
    }
}
