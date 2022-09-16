package com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.webclient.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum KakaoPayProperty {

    FAILED_POST("Post 요청 실패"),
    FAILED_GET("Get 요청 실패");

    private final String message;
}
