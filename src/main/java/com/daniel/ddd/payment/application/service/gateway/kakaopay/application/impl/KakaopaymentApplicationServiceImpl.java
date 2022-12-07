package com.daniel.ddd.payment.application.service.gateway.kakaopay.application.impl;

import com.daniel.ddd.global.error.exception.BadRequestException;
import com.daniel.ddd.global.redis.store.RedisStore;
import com.daniel.ddd.payment.application.service.gateway.kakaopay.application.KakaoPaymentApplicationService;
import com.daniel.ddd.payment.application.service.gateway.kakaopay.webclient.KakaoPayClient;
import com.daniel.ddd.payment.application.service.gateway.kakaopay.webclient.config.KakaoPayClientProperty;
import com.daniel.ddd.payment.application.service.gateway.kakaopay.webclient.model.KakaoPayRequest;
import com.daniel.ddd.payment.application.service.gateway.kakaopay.webclient.model.KakaoPayResponse;
import com.daniel.ddd.payment.application.service.redis.kakaopay.model.KakaoPayRedisParam;
import com.daniel.ddd.payment.model.dto.request.ItemPayRequestDto;
import com.daniel.ddd.payment.model.dto.request.PayCancelRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 카카오페이 결제 서비스 API 호출
 */
@Service
@RequiredArgsConstructor
public class KakaopaymentApplicationServiceImpl implements KakaoPaymentApplicationService {

    private final KakaoPayClient kakaoPayClient;
    private final KakaoPayClientProperty kakaoPayClientProperty;
    private final RedisStore kakaopayRedisStore;


    @Override
    public KakaoPayResponse.OrderInfoResponse getOrderInfo(String franchiseeId, String payId, String requestUrl) {
        KakaoPayRequest.OrderInfoRequest request = createOrderInfoRequest(franchiseeId, payId);
        return kakaoPayClient.getOrderInfo(requestUrl, request);
    }

    @Override
    public KakaoPayResponse.PayReadyResponse payItems(List<ItemPayRequestDto> itemPayRequestDtos, String requestUrl, String loginId) {
        KakaoPayRequest.PayReadyRequest request = createItemsPayRequestModel(itemPayRequestDtos, requestUrl, loginId);
        KakaoPayResponse.PayReadyResponse response = kakaoPayClient.ready(kakaoPayClientProperty.getUri().getReady(), request);
        savePayableData(response, request, loginId);
        return response;
    }

    @Override
    public KakaoPayResponse.PayApproveResponse completePayment(String payToken, String loginId) {
        KakaoPayRedisParam entity = kakaopayRedisStore.getData(loginId, KakaoPayRedisParam.class)
                .orElseThrow(() -> new BadRequestException("유효하지 않은 로그인 아이디"));
        KakaoPayRequest.PayApproveRequest request = createPayApproveRequest(payToken, entity, loginId);
        return kakaoPayClient.approve(kakaoPayClientProperty.getUri().getApprove(), request);
    }

    @Override
    public KakaoPayResponse.PayCancelResponse cancelPayment(PayCancelRequestDto payCancelRequestDto) {
        KakaoPayRequest.PayCancelRequest request = createPayCancelRequest(payCancelRequestDto);
        return kakaoPayClient.cancel(kakaoPayClientProperty.getUri().getCancel(), request);
    }

    private void savePayableData(KakaoPayResponse.PayReadyResponse response, KakaoPayRequest.PayReadyRequest request, String loginId) {
        KakaoPayRedisParam entity = KakaoPayRedisParam.of(
                loginId, response.getTid(),
                request.getPartnerOrderId(),
                request.getTotalAmount());
        kakaopayRedisStore.setData(entity.getLoginId(), entity);
    }

    private KakaoPayRequest.OrderInfoRequest createOrderInfoRequest(String franchiseeId, String payId) {
        return KakaoPayRequest.OrderInfoRequest.builder()
                .cid(franchiseeId)
                .tid(payId)
                .build();
    }

    private KakaoPayRequest.PayReadyRequest createItemsPayRequestModel(List<ItemPayRequestDto> itemPayRequestDtos, String requestURL, String loginId) {
        String itemsName = itemPayRequestDtos.get(0).getItemName() + "그 외 " + (itemPayRequestDtos.size() - 1) + "개";
        String orderId = loginId + " / " + itemsName;

        long totalAmount = itemPayRequestDtos.stream()
                .mapToLong(ItemPayRequestDto::getTotalAmount)
                .sum();

        String numberCode = String.join(",", itemPayRequestDtos.stream()
                .map(ItemPayRequestDto::getItemNumber)
                .map(String::valueOf)
                .toArray(String[]::new));
        String quantityCode = String.join(",", itemPayRequestDtos.stream()
                .map(ItemPayRequestDto::getQuantity)
                .map(String::valueOf)
                .toArray(String[]::new));

        String itemCode = numberCode + "/" + quantityCode;

        return KakaoPayRequest.PayReadyRequest.builder()
                .partnerOrderId(orderId)
                .partnerUserId(loginId)
                .itemName(itemsName)
                .itemCode(itemCode)
                .quantity(itemPayRequestDtos.size())
                .totalAmount((int) totalAmount)
                .taxFreeAmount(kakaoPayClientProperty.getParameter().getTaxFree())
                .cid(kakaoPayClientProperty.getParameter().getCid())
                .approvalUrl(requestURL + kakaoPayClientProperty.getApi().getApproval())
                .cancelUrl(requestURL + kakaoPayClientProperty.getApi().getCancel())
                .failUrl(requestURL + kakaoPayClientProperty.getApi().getFail())
                .build();
    }

    private KakaoPayRequest.PayApproveRequest createPayApproveRequest(String payToken, KakaoPayRedisParam entity, String loginId) {
        String tid = entity.getTid();
        String orderId = entity.getPartnerOrderId();
        Integer totalPrice = entity.getTotalAmount();

        return KakaoPayRequest.PayApproveRequest.builder()
                .cid(kakaoPayClientProperty.getParameter().getCid())
                .tid(tid)
                .partnerOrderId(orderId)
                .partnerUserId(loginId)
                .pgToken(payToken)
                .totalAmount(totalPrice)
                .build();
    }

    private KakaoPayRequest.PayCancelRequest createPayCancelRequest(PayCancelRequestDto payCancelRequestDto) {
        return KakaoPayRequest.PayCancelRequest.builder()
                .cid(kakaoPayClientProperty.getParameter().getCid())
                .tid(payCancelRequestDto.getPayId())
                .cancelAmount(payCancelRequestDto.getCancelAmount())
                .cancelTaxFreeAmount(payCancelRequestDto.getCancelTaxFreeAmount())
                .build();
    }

}
