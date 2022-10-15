package com.daniel.mychickenbreastshop.usecase.orderpayment.redis.kakao.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class KakaoPayParamsRedisEntity {

    private String loginId;
    private String tid;
    private String partnerOrderId;
    private Integer totalAmount;

    public static KakaoPayParamsRedisEntity of(String loginId, String tid, String partnerOrderId, Integer totalAmount) {
        return new KakaoPayParamsRedisEntity(loginId, tid, partnerOrderId, totalAmount);
    }
}
