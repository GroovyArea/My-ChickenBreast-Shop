package com.daniel.mychickenbreastshop.global.aspect.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLocked {

    /**
     * 사용자 정의 redisson lock Key
     */
    String key() default "";

    /**
     * redisson lock 유지 시간 <br>
     * 단위 : milliseconds
     */
    long leaseTime() default 3000;

    /**
     * redisson lock 획득 대기 시간 <br>
     * 단위 : milliseconds
     */
    long waitTime() default 3000;

    /**
     * aop 처리시 트랜잭션 적용 여부
     */
    boolean transactional() default false;

}

