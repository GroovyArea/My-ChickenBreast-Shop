package com.daniel.mychickenbreastshop.global.aspect;

import com.daniel.mychickenbreastshop.global.aspect.annotation.RedisLocked;
import com.daniel.mychickenbreastshop.global.error.exception.InternalErrorException;
import com.daniel.mychickenbreastshop.global.redis.util.RedisFunctionProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RTransaction;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RedisLockAspect {

    private static final String LOCK_SUFFIX = ":lock";

    private final RedissonClient redissonClient;
    private final RedisFunctionProvider redisFunctionProvider;


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

        RTransaction transaction = redisFunctionProvider.startRedisTransacton();
        TransactionStatus status = redisFunctionProvider.startDBTransacton();
        Object result;

        try {
            boolean tryLock =  redisFunctionProvider.tryLock(key, TimeUnit.MILLISECONDS, waitTime, leaseTime);

            if (!tryLock) {
                log.error("Lock 획득에 실패했습니다.");
            }

            log.info("락 획득 후 메서드 실행");
            result = joinPoint.proceed();

            redisFunctionProvider.commitRedis(transaction);
            redisFunctionProvider.rollbackDB(status);
            log.info("커밋했냐?");
        } catch (Throwable e) {
            redisFunctionProvider.rollbackRedis(transaction);
            redisFunctionProvider.rollbackDB(status);
            throw new InternalErrorException(e);
        } finally {
            if(redisFunctionProvider.canUnlock(key)) {
                redisFunctionProvider.unlock(key);
            }
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
