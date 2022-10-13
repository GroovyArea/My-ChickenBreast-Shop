package com.daniel.mychickenbreastshop.global.aspect.redisson;

import com.daniel.mychickenbreastshop.global.aspect.annotation.RedisLocked;
import com.daniel.mychickenbreastshop.global.error.exception.InternalErrorException;
import com.daniel.mychickenbreastshop.global.redis.function.RedisFunctionProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
@Order(value = Integer.MIN_VALUE)
public class RedisLockAspect {

    private static final String LOCK_SUFFIX = " :lock";

    private final RedisFunctionProvider redisFunctionProvider;

    @Around("@annotation(com.daniel.mychickenbreastshop.global.aspect.annotation.RedisLocked)")
    public Object executeWithLock(ProceedingJoinPoint joinPoint) {
        String key = getLockableKey(joinPoint);
        return execute(key, joinPoint);
    }

    private String getLockableKey(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        return method.getAnnotation(RedisLocked.class).key() + LOCK_SUFFIX;
    }

    private Object execute(String key, ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        long leaseTime = method.getAnnotation(RedisLocked.class).leaseTime();
        long waitTime = method.getAnnotation(RedisLocked.class).waitTime();

        Object result;

        try {
            boolean tryLock = redisFunctionProvider.tryLock(key, TimeUnit.MILLISECONDS, waitTime, leaseTime);

            if (!tryLock) {
                throw new InternalErrorException("Redis locked failed!");
            }

            result = joinPoint.proceed();

        } catch (Throwable e) {
            throw new InternalErrorException(e);
        } finally {
            if (redisFunctionProvider.canUnlock(key)) {
                redisFunctionProvider.unlock(key);
            }
            log.info("Redis unlocked!");
        }
        return result;
    }
}
