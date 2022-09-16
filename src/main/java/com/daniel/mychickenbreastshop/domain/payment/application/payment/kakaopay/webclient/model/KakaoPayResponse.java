package com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.webclient.model;

import com.daniel.mychickenbreastshop.domain.payment.domain.order.dto.response.kakaopay.ApprovedCancelAmount;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class KakaoPayResponse {

    @Getter
    @JsonInclude(NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class OrderInfoResponse {

        String cid; // 가맹점 코드
        String cidSecret;
        String tid; // 결제 고유 번호
        String status; // 결제 상태
        String partnerOrderId; // 가맹점 주문 번호
        String partnerUserId; // 가맹점 회원 ID
        String paymentMethodType; // 결제 수단, CARD or MONEY
        Amount amount; // 결제 금액
        CanceledAmount canceledAmount; // 취소된 금액
        CancelAvailableAmount cancelAvailableAmount; // 취소 가능 금액
        String itemName; // 상품 이름
        String itemCode; // 상품 코드
        Integer quantity; // 상품 수량

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        LocalDateTime createdAt; // 결제 준비 요청 시각
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        LocalDateTime approvedAt; // 결제 승인 시각
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        LocalDateTime canceledAt; // 결제 취소 시각

        SelectedCardInfo selectedCardInfo; // 결제 카드 정보
        PaymentActionDetails[] paymentActionDetails; // 결제/취소 상세

    }

    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Amount {

        private Integer total; // 전체 결제 금액
        private Integer taxFree; // 비과세 금액
        private Integer vat; // 부가세 금액
        private Integer point; // 포인트 금액
        private Integer discount; // 할인 금액
        private Integer greenDeposit; // 컵 보증금
    }

    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class CanceledAmount {

        private Integer total; // 전체 취소 금액
        private Integer taxFree; // 취소된 비과세 금액
        private Integer vat; // 취소된 부가세 금액
        private Integer point; // 취소된 포인트 금액
        private Integer discount; // 취소된 할인 금액
        private Integer greenDeposit; // 컵 보증금
    }

    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class CancelAvailableAmount {

        private Integer total; // 전체 취소 가능 금액
        private Integer taxFree; // 취소 가능한 비과세 금액
        private Integer vat; // 취소 가능한 부가세 금액
        private Integer point; // 취소 가능한 포인트 금액
        private Integer discount; // 취소 가능한 할인 금액
        private Integer greenDeposit; // 컵 보증금
    }

    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SelectedCardInfo {

        private String cardBin; // 카드 BIN
        private String cardCorpName; // 할부 개월 수
        private String interestFreeInstall; // 카드사 정보
        private Integer installMonth; // 무이자할부 여부 (Y/N)
    }

    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class PaymentActionDetails {

        private String aid; // Request 고유 번호
        private String approvedAt; // 거래 시간
        private String paymentActionType; // 결제 타입 PAYMENT(결제), CANCEL(결제취소), ISSUED_SID(SID 발급) 중 하나
        private String payload; // Request 로 전달한 값
        private Integer amount; // 결제/취소 총액
        private Integer pointAmount; // 결제/취소 포인트 금액
        private Integer discountAmount; // 할인 금액
        private Integer greenDeposit; // 컵 보증금
    }

    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(NON_NULL)
    public static class PayReadyResponse {

        private String tid; // 결제 고유 번호
        private String nextRedirectAppUrl; // 요청한 클라이언트(client)가 모바일 앱일 경우 카카오톡 결제 페이지 Redirect Url
        private String nextRedirectMobileUrl; // 모바일 웹일 경우 결제 페이지 Redirect Url
        private String nextRedirectPcUrl; // PC 웹일 경우 결제 요청 메시지(TMS)를 보내기 위한 정보 입력 화면 Redirect Url
        private String androidAppScheme; // 결제 화면 이동하는 Android 앱 스킴
        private String iosAppScheme; // 결제 화명 이동하는 IOS 앱 스킴
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        private LocalDateTime createdAt; // 결제 준비 요청 시간
    }

    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(NON_NULL)
    public static class PayApproveResponse {

        private String aid; // 요청 고유 번호
        private String tid; // 결제 고유 번호
        private String cid; // 가맹점 고유 번호
        private String sid; // 정기결제용 ID, 정기결제 CID로 단건결제 요청 시 발급
        private String partnerOrderId; // 가맹점 주문번호
        private String partnerUserId; // 가맹점 회원 Id
        private String paymentMethodType; // 결제 수단, CARD 또는 MONEY 중 하나
        private Amount amount; // 결제 금액 정보
        private CardInfo cardInfo; // 결제 상세 정보, 결제수단이 카드일 경우만 포함
        private String itemName; // 상품 이름
        private String itemCode; // 상품 코드
        private Integer quantity; // 상품 수량

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        private LocalDateTime createdAt; // 결제 준비 요청 시각
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        private LocalDateTime approvedAt; // 결제 승인 시간

        private String payload; // 결제 승인 요청에 대해 저장한 값, 요청 시 전달된 내용
    }


    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class CardInfo {

        private String purchaseCorp; // 매입 카드사 한글명
        private String purchaseCorpCode; // 매입 카드사 코드
        private String issuerCorp; // 카드 발급사 한글명
        private String issuerCorpCode; // 카드 발급사 코드
        private String kakaopayPurchaseCorp; // 카카오페이 매입사명
        private String kakaopayPurchaseCorpCode; // 카카도페이 매입사 코드
        private String kakaopayIssuerCorp; // 카카오페이 발급사명
        private String kakaopayIssuerCorpCode; // 카카오페이 발급사 코드
        private String bin; // 카드 BIN
        private String cardType; // 카드 타입
        private String installMonth; // 할부 개월 수
        private String approvedId; // 카드사 승인번호
        private String cardMid; // 카드사 가맹점 번호
        private String interestFreeInstall; // 무이자할부 여부 (Y/N)
        private String cardItemCode; // 카드 상품 코드
    }

    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(NON_NULL)
    public static class PayCancelResponse {

        private String aid; // 요청 고유 번호
        private String tid; // 결제 고유 번호
        private String cid; // 가맹점 코드
        private String status; // 결제 상태
        private String partnerOrderId; // 가맹점 주문 번호
        private String partnerUserId; // 가맹점 회원 id
        private String paymentMethodType; // 결제 수단, CARD 또는 MONEY 중 하나
        private Amount amount; // 결제 금액 정보
        private ApprovedCancelAmount approvedCancelAmount; // 이번 요청으로 취소된 금액
        private CanceledAmount canceledAmount; // 누계 취소 금액
        private CancelAvailableAmount cancelAvailableAmount; // 남은 취소 가능 금액
        private String itemName; // 상품 이름
        private String itemCode; // 상품 코드
        private Integer quantity; // 상품 수량

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        private LocalDateTime createdAt; // 결제 준비 요청 시각
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        private LocalDateTime approvedAt; // 결제 승인 시간
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        private LocalDateTime canceledAt; // 결제 준비 요청 시각
    }
}
