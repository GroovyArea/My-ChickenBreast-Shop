package com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient;

import com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.error.KakaoPayException;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient.config.KakaoPayClientProperty;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient.model.KakaoPayErrorProperty;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient.model.KakaoPayRequest;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient.model.KakaoPayResponse;
import com.daniel.mychickenbreastshop.global.util.ParamConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * kakaoPay Web Client
 */
@Component
@RequiredArgsConstructor
public class KakaoPayClient {

    private final KakaoPayClientProperty kakaoPayClientProperty;
    private final WebClient kakaoPayWebClient;
    private final ObjectMapper objectMapper;

    public KakaoPayResponse.OrderInfoResponse getOrderInfo(String uri, KakaoPayRequest.OrderInfoRequest orderInfoRequest) {
        return kakaoPayWebClient.post()
                .uri(uri)
                .headers(httpHeaders -> httpHeaders.addAll(setHeaders()))
                .bodyValue(ParamConverter.convert(objectMapper, orderInfoRequest))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new KakaoPayException(KakaoPayErrorProperty.FAILED_POST.getMessage())))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new KakaoPayException(KakaoPayErrorProperty.FAILED_POST.getMessage())))
                .bodyToMono(KakaoPayResponse.OrderInfoResponse.class)
                .block();
    }

    public KakaoPayResponse.PayReadyResponse ready(String uri, KakaoPayRequest.PayReadyRequest payReadyRequest) {
        return kakaoPayWebClient.post()
                .uri(uri)
                .headers(httpHeaders -> httpHeaders.addAll(setHeaders()))
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(ParamConverter.convert(objectMapper, payReadyRequest))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new KakaoPayException(KakaoPayErrorProperty.FAILED_POST.getMessage())))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new KakaoPayException(KakaoPayErrorProperty.FAILED_POST.getMessage())))
                .bodyToMono(KakaoPayResponse.PayReadyResponse.class)
                .block();
    }

    public KakaoPayResponse.PayApproveResponse approve(String uri, KakaoPayRequest.PayApproveRequest payApproveRequest) {
        return kakaoPayWebClient.post()
                .uri(uri)
                .headers(httpHeaders -> httpHeaders.addAll(setHeaders()))
                .bodyValue(ParamConverter.convert(objectMapper, payApproveRequest))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new KakaoPayException(KakaoPayErrorProperty.FAILED_POST.getMessage())))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new KakaoPayException(KakaoPayErrorProperty.FAILED_POST.getMessage())))
                .bodyToMono(KakaoPayResponse.PayApproveResponse.class)
                .block();
    }

    public KakaoPayResponse.PayCancelResponse cancel(String uri, KakaoPayRequest.PayCancelRequest payCancelRequest) {
        return kakaoPayWebClient.post()
                .uri(uri)
                .headers(httpHeaders -> httpHeaders.addAll(setHeaders()))
                .bodyValue(ParamConverter.convert(objectMapper, payCancelRequest))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new KakaoPayException(KakaoPayErrorProperty.FAILED_POST.getMessage())))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new KakaoPayException(KakaoPayErrorProperty.FAILED_POST.getMessage())))
                .bodyToMono(KakaoPayResponse.PayCancelResponse.class)
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
