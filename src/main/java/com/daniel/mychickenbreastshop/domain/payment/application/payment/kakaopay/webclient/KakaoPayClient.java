package com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.webclient;

import com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.error.KakaoPayException;
import com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.webclient.model.KakaoPayRequest.OrderInfoRequest;
import com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.webclient.model.KakaoPayRequest.PayApproveRequest;
import com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.webclient.model.KakaoPayRequest.PayCancelRequest;
import com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.webclient.model.KakaoPayResponse.OrderInfoResponse;
import com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.webclient.model.KakaoPayResponse.PayCancelResponse;
import com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.webclient.model.KakaoPayResponse.PayReadyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

import static com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.webclient.model.KakaoPayProperty.FAILED_POST;
import static com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.webclient.model.KakaoPayRequest.PayReadyRequest;
import static com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.webclient.model.KakaoPayResponse.PayApproveResponse;

/**
 * kakao pay Web Client
 */
@Component
@RequiredArgsConstructor
public class KakaoPayClient {

    @Value("${kakaoPay.admin.key}")
    private String adminKey;

    private final WebClient kakaoPayWebClient;

    public OrderInfoResponse getOrderInfo(String uri, OrderInfoRequest orderInfoRequest) {
        return kakaoPayWebClient.post()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> httpHeaders.addAll(setHeaders()))
                .acceptCharset(StandardCharsets.UTF_8)
                .bodyValue(orderInfoRequest)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new KakaoPayException(FAILED_POST.getMessage())))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new KakaoPayException(FAILED_POST.getMessage())))
                .bodyToMono(OrderInfoResponse.class)
                .block();
    }

    public PayReadyResponse ready(String uri, PayReadyRequest payReadyRequest) {
        return kakaoPayWebClient.post()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> httpHeaders.addAll(setHeaders()))
                .acceptCharset(StandardCharsets.UTF_8)
                .bodyValue(payReadyRequest)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new KakaoPayException(FAILED_POST.getMessage())))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new KakaoPayException(FAILED_POST.getMessage())))
                .bodyToMono(PayReadyResponse.class)
                .block();
    }

    public PayApproveResponse approve(String uri, PayApproveRequest payApproveRequest) {
        return kakaoPayWebClient.post()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> httpHeaders.addAll(setHeaders()))
                .acceptCharset(StandardCharsets.UTF_8)
                .bodyValue(payApproveRequest)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new KakaoPayException(FAILED_POST.getMessage())))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new KakaoPayException(FAILED_POST.getMessage())))
                .bodyToMono(PayApproveResponse.class)
                .block();
    }

    public PayCancelResponse cancel(String uri, PayCancelRequest payCancelRequest) {
        return kakaoPayWebClient.post()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> httpHeaders.addAll(setHeaders()))
                .acceptCharset(StandardCharsets.UTF_8)
                .bodyValue(payCancelRequest)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new KakaoPayException(FAILED_POST.getMessage())))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new KakaoPayException(FAILED_POST.getMessage())))
                .bodyToMono(PayCancelResponse.class)
                .block();
    }

    private HttpHeaders setHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + adminKey);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
        return headers;
    }

}
