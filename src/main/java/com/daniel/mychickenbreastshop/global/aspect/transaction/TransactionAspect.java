package com.daniel.mychickenbreastshop.global.aspect.transaction;

import com.daniel.mychickenbreastshop.global.aspect.annotation.RedisLocked;
import com.daniel.mychickenbreastshop.global.error.exception.InternalErrorException;
import com.daniel.mychickenbreastshop.global.redis.util.RedisFunctionProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RTransaction;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.lang.reflect.Method;

/**
 * 트랜잭션 처리 aop
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
@Order(value = 2)
public class TransactionAspect {

    private final RedisFunctionProvider redisFunctionProvider;

    @Around("@annotation(com.daniel.mychickenbreastshop.global.aspect.annotation.RedisLocked)")
    public Object executeWithTransaction(ProceedingJoinPoint joinPoint) {
        if (!isTransactional(joinPoint)) {
            try {
                return joinPoint.proceed();
            } catch (Throwable e) {
                throw new InternalErrorException(e);
            }
        }

        RTransaction transaction = redisFunctionProvider.startRedisTransacton();
        TransactionStatus status = redisFunctionProvider.startDBTransacton();

        Object result;

        try {
            result = joinPoint.proceed();

            redisFunctionProvider.commitRedis(transaction);
            redisFunctionProvider.commitDB(status);
        } catch (Throwable e) {
            redisFunctionProvider.rollbackRedis(transaction);
            redisFunctionProvider.rollbackDB(status);
            throw new InternalErrorException(e);
        }
        return result;
    }

    private boolean isTransactional(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(RedisLocked.class).transactional();
    }
}
