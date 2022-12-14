package com.daniel.mychickenbreastshop.payment.application.service.gateway.kakaopay.webclient.config;

import com.daniel.mychickenbreastshop.global.config.client.WebClientConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * 카카오페이 WebClient 생성
 */
@Configuration
@RequiredArgsConstructor
public class KakaoPayClientConfig extends WebClientConfig {

    private final KakaoPayClientProperty property;

    @Bean
    public WebClient kakaoPayWebClient() {
        return createWebClientFrame(property.getUrl(), property.getReadTime(), property.getConnectTime());
    }

}
