package com.daniel.mychickenbreastshop.global.config;

import com.daniel.mychickenbreastshop.global.config.model.EmailAsyncProperty;
import com.daniel.mychickenbreastshop.infra.async.error.handler.AsyncApiExceptionAdvice;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@RequiredArgsConstructor
public class AsyncConfig extends AsyncConfigurerSupport {

    private static final String EMAIL_THREAD_NAME_PREFIX = "email-async-";

    private final AsyncApiExceptionAdvice asyncApiExceptionAdvice;

    @Bean(name = "emailThreadPoolTaskExecutor")
    public Executor emailExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(EmailAsyncProperty.CORE_POOL_SIZE.getSize());
        executor.setMaxPoolSize(EmailAsyncProperty.MAX_POOL_SIZE.getSize());
        executor.setQueueCapacity(EmailAsyncProperty.QUEUE_CAPACITY.getSize());
        executor.setThreadNamePrefix(EMAIL_THREAD_NAME_PREFIX);
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return asyncApiExceptionAdvice;
    }
}
