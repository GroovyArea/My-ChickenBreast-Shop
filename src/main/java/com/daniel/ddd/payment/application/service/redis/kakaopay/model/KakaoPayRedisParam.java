package com.daniel.ddd.payment.application.service.redis.kakaopay.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class KakaoPayRedisParam {

    private String loginId;
    private String tid;
    private String partnerOrderId;
    private Integer totalAmount;

    public static KakaoPayRedisParam of(String loginId, String tid, String partnerOrderId, Integer totalAmount) {
        return new KakaoPayRedisParam(loginId, tid, partnerOrderId, totalAmount);
    }
}
