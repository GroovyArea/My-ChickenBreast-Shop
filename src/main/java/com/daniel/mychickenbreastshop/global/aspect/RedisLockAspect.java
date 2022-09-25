package com.daniel.mychickenbreastshop.global.aspect;

import org.aspectj.lang.ProceedingJoinPoint;

public interface RedisLockAspect {

    Object executeWithLock(ProceedingJoinPoint joinPoint);
}