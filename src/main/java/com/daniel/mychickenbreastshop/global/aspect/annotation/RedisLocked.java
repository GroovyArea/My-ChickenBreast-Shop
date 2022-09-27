package com.daniel.mychickenbreastshop.global.aspect.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLocked {

    /**
     * redisson lock 유지 시간 <br>
     * 단위 : milliseconds
     * @return
     */
    long leaseTime() default 1000;

    /**
     * redisson lock 획득 대기 시간 <br>
     * 단위 : milliseconds
     * @return
     */
    long waitTime() default 3000;
}
