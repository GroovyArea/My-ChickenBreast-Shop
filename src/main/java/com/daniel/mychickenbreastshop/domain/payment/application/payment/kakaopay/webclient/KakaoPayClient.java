package com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.webclient;

import com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.error.KakaoPayException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class KakaoPayClient {

    private final WebClient kakaoPayWebClient;



    public <T> T get() {
        kakaoPayWebClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .map(KakaoPayException::new))
                .bodyToMono(clazz)
                .block();
    }

    public <T>
}
