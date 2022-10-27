package com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.application.impl;

import com.daniel.mychickenbreastshop.global.redis.store.RedisStore;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.application.KakaoPaymentService;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient.model.KakaoPayResponse;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient.KakaoPayClient;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient.config.KakaoPayClientProperty;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient.model.KakaoPayRequest.PayApproveRequest;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient.model.KakaoPayRequest.PayCancelRequest;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient.model.KakaoPayRequest.PayReadyRequest;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient.model.KakaoPayResponse.PayReadyResponse;
import com.daniel.mychickenbreastshop.usecase.orderpayment.extract.model.CartValue;
import com.daniel.mychickenbreastshop.usecase.orderpayment.model.dto.request.ItemPayRequestDto;
import com.daniel.mychickenbreastshop.usecase.orderpayment.model.dto.request.PayCancelRequestDto;
import com.daniel.mychickenbreastshop.usecase.orderpayment.redis.kakao.model.KakaoPayParamsRedisEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 카카오페이 결제 서비스 API 호출
 */
@Service
@RequiredArgsConstructor
public class KakaopaymentServiceImpl implements KakaoPaymentService {

    private final KakaoPayClient kakaoPayClient;
    private final KakaoPayClientProperty kakaoPayClientProperty;
    private final RedisStore kakaopayRedisStore;


    @Override
    public KakaoPayResponse.OrderInfoResponse getOrderInfo(String franchiseeId, String payId) {
        return null;
    }

    @Override
    public PayReadyResponse payItem(ItemPayRequestDto itemPayRequestDto, String requestUrl, String loginId) {
        PayReadyRequest request = createItemPayRequest(itemPayRequestDto, requestUrl, loginId);
        PayReadyResponse response = kakaoPayClient.ready(kakaoPayClientProperty.getUri().getReady(), request);
        savePayableData(response, request, loginId);
        return response;
    }

    @Override
    public PayReadyResponse payCart(CartValue cartValue, String requestUrl, String loginId) {
        PayReadyRequest request = createCartPayRequest(cartValue, requestUrl, loginId);
        PayReadyResponse response = kakaoPayClient.ready(kakaoPayClientProperty.getUri().getReady(), request);
        savePayableData(response, request, loginId);
        return response;
    }

    @Override
    public KakaoPayResponse.PayApproveResponse completePayment(String payToken, String loginId) {
        KakaoPayParamsRedisEntity entity = kakaopayRedisStore.getData(loginId, KakaoPayParamsRedisEntity.class);
        PayApproveRequest request = createPayApproveRequest(payToken, entity, loginId);
        return kakaoPayClient.approve(kakaoPayClientProperty.getUri().getApprove(), request);
    }

    @Override
    public KakaoPayResponse.PayCancelResponse cancelPayment(PayCancelRequestDto payCancelRequestDto) {
        PayCancelRequest request = createPayCancelRequest(payCancelRequestDto);
        return kakaoPayClient.cancel(kakaoPayClientProperty.getUri().getCancel(), request);
    }

    private void savePayableData(PayReadyResponse response, PayReadyRequest request, String loginId) {
        KakaoPayParamsRedisEntity entity = KakaoPayParamsRedisEntity.of(loginId, response.getTid(), request.getPartnerOrderId(), request.getTotalAmount());
        kakaopayRedisStore.setData(entity.getLoginId(), entity);
    }

    private PayReadyRequest createItemPayRequest(ItemPayRequestDto dto, String requestUrl, String loginId) {
        String orderId = loginId + " / " + dto.getItemName();

        return PayReadyRequest.builder()
                .partnerOrderId(orderId)
                .partnerUserId(loginId)
                .itemName(dto.getItemName())
                .quantity(dto.getQuantity())
                .totalAmount(dto.getTotalAmount())
                .taxFreeAmount(kakaoPayClientProperty.getParameter().getTaxFree())
                .cid(kakaoPayClientProperty.getParameter().getCid())
                .approvalUrl(requestUrl + kakaoPayClientProperty.getApi().getApproval())
                .cancelUrl(requestUrl + kakaoPayClientProperty.getApi().getCancel())
                .failUrl(requestUrl + kakaoPayClientProperty.getApi().getFail())
                .build();
    }

    private PayReadyRequest createCartPayRequest(CartValue cartValue, String requestUrl, String loginId) {
        String itemName = cartValue.getItemNames().get(0) + " 그 외 " + (cartValue.getItemNames().size() - 1) + "개";
        String orderId = loginId + " / " + itemName;
        String numberCode = String.join(",", cartValue.getItemNumbers().stream().map(String::valueOf).toArray(String[]::new));
        String quantityCode = String.join(",", cartValue.getItemQuantities().stream().map(String::valueOf).toArray(String[]::new));
        String itemCode = numberCode + "/" + quantityCode;

        return PayReadyRequest.builder()
                .partnerOrderId(orderId)
                .partnerUserId(loginId)
                .itemName(itemName)
                .itemCode(itemCode)
                .quantity(cartValue.getItemQuantities().size())
                .totalAmount((int) cartValue.getTotalPrice())
                .taxFreeAmount(kakaoPayClientProperty.getParameter().getTaxFree())
                .cid(kakaoPayClientProperty.getParameter().getCid())
                .approvalUrl(requestUrl + kakaoPayClientProperty.getApi().getApproval())
                .cancelUrl(requestUrl + kakaoPayClientProperty.getApi().getCancel())
                .failUrl(requestUrl + kakaoPayClientProperty.getApi().getFail())
                .build();
    }

    private PayApproveRequest createPayApproveRequest(String payToken, KakaoPayParamsRedisEntity entity, String loginId) {
        String tid = entity.getTid();
        String orderId = entity.getPartnerOrderId();
        Integer totalPrice = entity.getTotalAmount();

        return PayApproveRequest.builder()
                .cid(kakaoPayClientProperty.getParameter().getCid())
                .tid(tid)
                .partnerOrderId(orderId)
                .partnerUserId(loginId)
                .pgToken(payToken)
                .totalAmount(totalPrice)
                .build();
    }

    private PayCancelRequest createPayCancelRequest(PayCancelRequestDto payCancelRequestDto) {
        return PayCancelRequest.builder()
                .cid(kakaoPayClientProperty.getParameter().getCid())
                .tid(payCancelRequestDto.getPayId())
                .cancelAmount(payCancelRequestDto.getCancelAmount())
                .cancelTaxFreeAmount(payCancelRequestDto.getCancelTaxFreeAmount())
                .build();
    }

}
