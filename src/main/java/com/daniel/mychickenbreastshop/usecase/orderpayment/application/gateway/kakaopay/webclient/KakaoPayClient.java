package com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient;

import com.daniel.mychickenbreastshop.global.util.ParamConverter;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.error.KakaoPayException;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient.config.KakaoPayClientProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient.model.KakaoPayErrorProperty.FAILED_POST;
import static com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient.model.KakaoPayRequest.*;
import static com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient.model.KakaoPayResponse.*;

/**
 * kakaoPay Web Client
 */
@Component
@RequiredArgsConstructor
public class KakaoPayClient {

    private final KakaoPayClientProperty kakaoPayClientProperty;
    private final WebClient kakaoPayWebClient;
    private final ObjectMapper objectMapper;

    public OrderInfoResponse getOrderInfo(String uri, OrderInfoRequest orderInfoRequest) {
        return kakaoPayWebClient.post()
                .uri(uri)
                .headers(httpHeaders -> httpHeaders.addAll(setHeaders()))
                .bodyValue(ParamConverter.convert(objectMapper, orderInfoRequest))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new KakaoPayException(FAILED_POST.getMessage())))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new KakaoPayException(FAILED_POST.getMessage())))
                .bodyToMono(OrderInfoResponse.class)
                .block();
    }

    public PayReadyResponse ready(String uri, PayReadyRequest payReadyRequest) {
        return kakaoPayWebClient.post()
                .uri(uri)
                .headers(httpHeaders -> httpHeaders.addAll(setHeaders()))
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(ParamConverter.convert(objectMapper, payReadyRequest))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new KakaoPayException(FAILED_POST.getMessage())))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new KakaoPayException(FAILED_POST.getMessage())))
                .bodyToMono(PayReadyResponse.class)
                .block();
    }

    public PayApproveResponse approve(String uri, PayApproveRequest payApproveRequest) {
        return kakaoPayWebClient.post()
                .uri(uri)
                .headers(httpHeaders -> httpHeaders.addAll(setHeaders()))
                .bodyValue(ParamConverter.convert(objectMapper, payApproveRequest))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new KakaoPayException(FAILED_POST.getMessage())))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new KakaoPayException(FAILED_POST.getMessage())))
                .bodyToMono(PayApproveResponse.class)
                .block();
    }

    public PayCancelResponse cancel(String uri, PayCancelRequest payCancelRequest) {
        return kakaoPayWebClient.post()
                .uri(uri)
                .headers(httpHeaders -> httpHeaders.addAll(setHeaders()))
                .bodyValue(ParamConverter.convert(objectMapper, payCancelRequest))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new KakaoPayException(FAILED_POST.getMessage())))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new KakaoPayException(FAILED_POST.getMessage())))
                .bodyToMono(PayCancelResponse.class)
                .block();
    }

    private HttpHeaders setHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoPayClientProperty.getAdmin().getKey());
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
        return headers;
    }

}
