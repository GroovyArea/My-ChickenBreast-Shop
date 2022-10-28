package com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.application.impl;

import com.daniel.mychickenbreastshop.global.redis.store.RedisStore;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient.KakaoPayClient;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient.config.KakaoPayClientProperty;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient.model.KakaoPayRequest.PayApproveRequest;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient.model.KakaoPayRequest.PayReadyRequest;
import com.daniel.mychickenbreastshop.usecase.orderpayment.extract.model.CartValue;
import com.daniel.mychickenbreastshop.usecase.orderpayment.model.dto.request.ItemPayRequestDto;
import com.daniel.mychickenbreastshop.usecase.orderpayment.model.dto.request.PayCancelRequestDto;
import com.daniel.mychickenbreastshop.usecase.orderpayment.redis.kakao.model.KakaoPayParamsRedisEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.util.List;

import static com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient.config.KakaoPayClientProperty.*;
import static com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient.model.KakaoPayRequest.OrderInfoRequest;
import static com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient.model.KakaoPayRequest.PayCancelRequest;
import static com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient.model.KakaoPayResponse.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class KakaopaymentServiceImplTest {

    @Mock
    private KakaoPayClient kakaoPayClient;

    @Mock
    private KakaoPayClientProperty kakaoPayClientProperty;

    @Mock
    private RedisStore kakaopayRedisStore;

    @InjectMocks
    private KakaopaymentServiceImpl kakaopaymentServiceImpl;

    String requestUrl;
    String loginId;
    Uri uri;
    Parameter parameter;
    Api api;

    @BeforeEach
    void setUp() {
        requestUrl = "localhost:8080/";
        loginId = "loginId";

        uri = new Uri();
        uri.setReady("kakaopay/ready");
        uri.setApprove("kakaopay/approve");
        uri.setCancel("kakaopay/cancel");
        uri.setOrder("kakaopay/order");

        parameter = new Parameter();
        parameter.setCid("testcid");
        parameter.setTaxFree(100);

        api = new Api();
        api.setApproval("/approve");
        api.setCancel("/cancel");
        api.setFail("fail");

        when(kakaoPayClientProperty.getApi()).thenReturn(api);
        when(kakaoPayClientProperty.getParameter()).thenReturn(parameter);
        when(kakaoPayClientProperty.getUri()).thenReturn(uri);

    }


    @DisplayName("카카오페이 api 요청을 통해 주문 정보를 반환한다.")
    @Test
    void getOrderInfo() {
        // given
        String franchiseeId = "12112";
        String payId = "b1212";

        OrderInfoRequest orderInfoRequest = OrderInfoRequest.builder()
                .cid(franchiseeId)
                .tid(payId)
                .build();

        OrderInfoResponse orderInfoResponse = createOrderInfoResponse();

        // when
        when(kakaoPayClient.getOrderInfo(anyString(), any(orderInfoRequest.getClass()))).thenReturn(orderInfoResponse);
        OrderInfoResponse orderInfo = kakaopaymentServiceImpl.getOrderInfo(franchiseeId, payId, requestUrl);

        assertThat(orderInfo).isEqualTo(orderInfoResponse);
    }

    @DisplayName("카카오페이 api 요청을 통해 단건 상품 결제 준비 요청 응답을 반환한다.")
    @Test
    void payItem() {
        // given
        ItemPayRequestDto itemPayRequestDto = ItemPayRequestDto.builder()
                .itemName("itemName")
                .itemNumber(1L)
                .quantity(10)
                .totalAmount(10000)
                .build();

        PayReadyResponse payReadyResponse = createPayReadyResponse();

        // when
        when(kakaoPayClient.ready(anyString(), any(PayReadyRequest.class))).thenReturn(payReadyResponse);


        assertThat(kakaopaymentServiceImpl.payItem(itemPayRequestDto, requestUrl, loginId)).isEqualTo(payReadyResponse);
    }

    @DisplayName("카카오페이 api 요청을 통해 장바구니에 담긴 상품 결제 준비 요청 응답을 반환한다.")
    @Test
    void payCart() {
        // given
        CartValue cartValue = CartValue.builder()
                .itemNumbers(List.of(1L, 2L, 3L, 4L))
                .itemNames(List.of("name1", "name2", "name3", "name4"))
                .itemQuantities(List.of(12, 13, 14, 15))
                .totalPrice(10000L)
                .build();

        PayReadyResponse payReadyResponse = createPayReadyResponse();

        // when
        when(kakaoPayClient.ready(anyString(), any(PayReadyRequest.class))).thenReturn(payReadyResponse);

        assertThat(kakaopaymentServiceImpl.payCart(cartValue, requestUrl, loginId)).isEqualTo(payReadyResponse);

    }

    @DisplayName("카카오페이 api 결제 승인 요청의 응답 객체를 반환한다.")
    @Test
    void completePayment() {
        // given
        String payToken = "payToken";

        KakaoPayParamsRedisEntity entity = KakaoPayParamsRedisEntity.of(loginId, "tid", "partnerOrderId", 10000);

        PayApproveResponse payApproveResponse = createPayApproveResponse();

        // when
        when(kakaopayRedisStore.getData(loginId, KakaoPayParamsRedisEntity.class)).thenReturn(entity);
        when(kakaoPayClient.approve(anyString(), any(PayApproveRequest.class))).thenReturn(payApproveResponse);

        assertThat(kakaopaymentServiceImpl.completePayment(payToken, loginId)).isEqualTo(payApproveResponse);

    }

    @DisplayName("카카오페이 api 결제 취소 요청의 응답 객쳋를 반환한다.")
    @Test
    void cancelPayment() {
        // given
        PayCancelRequestDto payCancelRequestDto = PayCancelRequestDto.builder()
                .payId("payId")
                .cancelAmount(10000)
                .cancelTaxFreeAmount(100)
                .build();

        PayCancelResponse payCancelResponse = createPayCancelResponse();

        // when
        when(kakaoPayClient.cancel(anyString(), any(PayCancelRequest.class))).thenReturn(payCancelResponse);

        assertThat(kakaopaymentServiceImpl.cancelPayment(payCancelRequestDto)).isEqualTo(payCancelResponse);
    }

    private OrderInfoResponse createOrderInfoResponse() {
        return OrderInfoResponse.builder()
                .cid("cid")
                .tid("tid")
                .status("status")
                .partnerOrderId("partnerOrderId")
                .partnerUserId("partnerUserId")
                .paymentMethodType("CARD")
                .amount(Amount.builder()
                        .total(10000)
                        .taxFree(1000)
                        .vat(1000)
                        .point(100)
                        .discount(10)
                        .build())
                .canceledAmount(CanceledAmount.builder()
                        .total(10000)
                        .taxFree(1000)
                        .vat(1000)
                        .point(100)
                        .discount(10)
                        .build())
                .itemName("itemName")
                .itemCode("itemCode")
                .quantity(2)
                .createdAt(LocalDateTime.now())
                .approvedAt(LocalDateTime.now().plusSeconds(1000))
                .canceledAt(LocalDateTime.now().plusDays(5))
                .selectedCardInfo(SelectedCardInfo.builder()
                        .cardBin("101212")
                        .cardCorpName("12")
                        .interestFreeInstall("kakao")
                        .installMonth(0)
                        .build())
                .paymentActionDetails(new PaymentActionDetails[]{PaymentActionDetails.builder()
                        .aid("aid")
                        .approvedAt(String.valueOf(LocalDateTime.now()))
                        .paymentActionType("PAYMENT")
                        .payload("payload")
                        .pointAmount(10000)
                        .discountAmount(10)
                        .build()})
                .build();
    }

    private PayReadyResponse createPayReadyResponse() {
        return PayReadyResponse.builder()
                .tid("tid")
                .nextRedirectPcUrl("localhost:8080/redirectUrl")
                .createdAt(LocalDateTime.now())
                .build();
    }

    private PayApproveResponse createPayApproveResponse() {
        return PayApproveResponse.builder()
                .aid("aid")
                .tid("tid")
                .cid("cid")
                .sid("sid")
                .partnerOrderId("partnerOrderId")
                .partnerUserId("partnerUserId")
                .paymentMethodType("CARD")
                .amount(Amount.builder()
                        .total(10000)
                        .taxFree(1000)
                        .vat(1000)
                        .point(100)
                        .discount(10)
                        .build())
                .cardInfo(CardInfo.builder()
                        .purchaseCorp("kakao")
                        .purchaseCorpCode("1234")
                        .issuerCorp("corp")
                        .issuerCorpCode("123")
                        .bin("bin")
                        .cardType("HANA")
                        .interestFreeInstall("kakao")
                        .installMonth("12")
                        .build())
                .itemName("itemName")
                .itemCode("12")
                .quantity(4)
                .createdAt(LocalDateTime.now())
                .approvedAt(LocalDateTime.now().plusSeconds(120))
                .payload("payload")
                .build();
    }

    private PayCancelResponse createPayCancelResponse() {
        return PayCancelResponse.builder()
                .cid("cid")
                .tid("tid")
                .status("status")
                .partnerOrderId("partnerOrderId")
                .partnerUserId("partnerUserId")
                .paymentMethodType("CARD")
                .amount(Amount.builder()
                        .total(10000)
                        .taxFree(1000)
                        .vat(1000)
                        .point(100)
                        .discount(10)
                        .build())
                .canceledAmount(CanceledAmount.builder()
                        .total(10000)
                        .taxFree(1000)
                        .vat(1000)
                        .point(100)
                        .discount(10)
                        .build())
                .cancelAvailableAmount(CancelAvailableAmount.builder()
                        .total(10000)
                        .taxFree(1000)
                        .vat(1000)
                        .point(100)
                        .discount(10)
                        .build())
                .itemName("itemName")
                .itemCode("itemCode")
                .quantity(2)
                .createdAt(LocalDateTime.now())
                .approvedAt(LocalDateTime.now().plusSeconds(1000))
                .canceledAt(LocalDateTime.now().plusDays(5))
                .build();
    }
}