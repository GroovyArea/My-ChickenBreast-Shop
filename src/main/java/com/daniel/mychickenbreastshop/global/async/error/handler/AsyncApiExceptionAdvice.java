package com.daniel.mychickenbreastshop.global.async.error.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


@Component
@Slf4j
public class AsyncApiExceptionAdvice implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        log.error("Exception message : '" + ex.getMessage() + "'", ex);
        log.error("Method name : '" + method.getName() + "'");
    }
}
