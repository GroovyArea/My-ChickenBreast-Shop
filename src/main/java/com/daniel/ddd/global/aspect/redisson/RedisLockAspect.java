package com.daniel.ddd.global.aspect.redisson;

import com.daniel.ddd.global.aspect.annotation.RedisLocked;
import com.daniel.ddd.global.error.exception.InternalErrorException;
import com.daniel.ddd.global.redis.function.RedissonFunctionProvider;
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

    private final RedissonFunctionProvider redissonFunctionProvider;

    @Around("@annotation(com.daniel.ddd.global.aspect.annotation.RedisLocked)")
    public Object executeWithLock(ProceedingJoinPoint joinPoint) {
        String key = getLockableKey(joinPoint);
        return execute(key, joinPoint);
    }

    private String getLockableKey(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        return method.getAnnotation(RedisLocked.class).lockKey() + LOCK_SUFFIX;
    }

    private Object execute(String key, ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        long leaseTime = method.getAnnotation(RedisLocked.class).leaseTime();
        long waitTime = method.getAnnotation(RedisLocked.class).waitTime();

        Object result;

        try {
            boolean tryLock = redissonFunctionProvider.tryLock(key, TimeUnit.MILLISECONDS, waitTime, leaseTime);

            if (!tryLock) {
                throw new InternalErrorException("Redis locked failed!");
            }

            result = joinPoint.proceed();

        } catch (Throwable e) {
            throw new InternalErrorException(e);
        } finally {
            if (redissonFunctionProvider.canUnlock(key)) {
                redissonFunctionProvider.unlock(key);
            }
            log.info("Redis unlocked!");
        }
        return result;
    }
}
