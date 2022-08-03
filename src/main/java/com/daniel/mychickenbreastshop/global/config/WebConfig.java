package com.daniel.mychickenbreastshop.global.config;

import com.daniel.mychickenbreastshop.global.auth.interceptor.AuthorizeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 설정 클래스
 *
 * <pre>
 *     <b>history</b>
 *     1.0, 2022.08.03 최초 작성
 * </pre>
 *
 * @author 김남영
 * @version 1.0
 */
@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthorizeInterceptor authorizeInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizeInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/user");
    }
}
