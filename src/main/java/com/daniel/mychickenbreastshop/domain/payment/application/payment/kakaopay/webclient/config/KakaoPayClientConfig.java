package com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.webclient.config;

import com.daniel.mychickenbreastshop.global.config.client.WebClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class KakaoPayClientConfig extends WebClientConfig {

    @Value("${kakaopay.url}")
    private String baseUrl;

    @Value("${kakaopay.readTime}")
    private int readTime;

    @Value("${kakaopay.connectTime}")
    private int connectTime;

    @Bean
    public WebClient kakaoPayWebClient() {
        return createWebClientFrame(baseUrl, readTime, connectTime);
    }

}
