package com.daniel.mychickenbreastshop.global.aspect;

import com.daniel.mychickenbreastshop.global.aspect.annotation.RedisLocked;
import com.daniel.mychickenbreastshop.global.error.exception.InternalErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RedisLockAspect {

    private final RedissonClient redissonClient;

    private static final String LOCK_SUFFIX = ":lock";

    @Around("@annotation(com.daniel.mychickenbreastshop.global.aspect.annotation.RedisLocked)")
    public Object executeWithLock(ProceedingJoinPoint joinPoint) {
        String key = getLockableKey(joinPoint);
        return execute(key, joinPoint);
    }


    private String getLockableKey(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        return args[0] + LOCK_SUFFIX;
    }

    private Object execute(String key, ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        long leaseTime = method.getAnnotation(RedisLocked.class).leaseTime();
        long waitTime = method.getAnnotation(RedisLocked.class).waitTime();

        RLock lock = redissonClient.getLock(key);

        Object result;

        try {
            boolean tryLock = lock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS);

            if (!tryLock) {
                log.error("Lock 획득에 실패했습니다.");
            }

            result = joinPoint.proceed();
        } catch (Throwable e) {
            throw new InternalErrorException(e);
        } finally {
            unlock(lock);
            log.info("Redis unlocked!");
        }
        return result;
    }

    private void unlock(RLock lock) {
        if (lock != null && lock.isLocked()) {
            lock.unlock();
        }
    }
}
