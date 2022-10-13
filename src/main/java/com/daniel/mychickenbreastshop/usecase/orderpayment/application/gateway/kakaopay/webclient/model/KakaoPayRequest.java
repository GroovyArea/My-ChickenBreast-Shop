package com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

/**
 * KakaoPay API 요청 모델
 */
public class KakaoPayRequest {

    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class OrderInfoRequest {

        private String cid;// 가맹점 코드, 10자
        private String tid;// 결제 고유번호, 20자

    }

    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class PayReadyRequest {

        private String cid; // 가맹점 코드
        private String partnerOrderId; // 가맹점 주문 번호
        private String partnerUserId; // 가맹점 회원 ID
        private String itemName; // 상품명
        private String itemCode; // 상품 코드
        private Integer quantity; // 상품 수량
        private Integer totalAmount; // 상품 총액
        private Integer taxFreeAmount; // 상품 비과세 금액
        private String approvalUrl; // 결제 성공 시 redirect url
        private String cancelUrl; // 결제 취소 시 redirect url
        private String failUrl; // 결제 실패 시 redirect url
    }

    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class PayApproveRequest {

        private String cid;// 가맹점 코드
        private String tid; // 결제 고유 번호 (결제 준비 API 응답에 포함)
        private String partnerOrderId; // 가맹점 주문 번호, 결제 준비 API 요청과 일치해야 함
        private String partnerUserId; // 가맹점 회원 id, 결제 준비 API 요청과 일치해야 함
        private String pgToken;  // 결제승인 요청을 인증하는 토큰 사용자 결제 수단 선택 완료 시, approval_url로 redirection해줄 때 pg_token을 query string으로 전달
        private Integer totalAmount; // 상품 총액, 결제 준비 API 요청과 일치해야 함
    }

    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class PayCancelRequest {

        private String cid; // 가맹점 코드
        private String tid; // 결제 고유번호
        private Integer cancelAmount; // 취소 금액
        private Integer cancelTaxFreeAmount; // 취소 비과세 금액
    }


}
