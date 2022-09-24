package com.daniel.mychickenbreastshop.global.aspect;

import org.aspectj.lang.ProceedingJoinPoint;

public interface RedisLockAspect {

    ProceedingJoinPoint executeWithLock(ProceedingJoinPoint joinPoint);

}
