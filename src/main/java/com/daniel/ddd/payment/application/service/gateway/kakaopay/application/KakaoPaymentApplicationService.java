package com.daniel.ddd.payment.application.service.gateway.kakaopay.application;

import com.daniel.ddd.payment.application.service.gateway.kakaopay.webclient.model.KakaoPayResponse;
import com.daniel.ddd.payment.model.dto.request.ItemPayRequestDto;
import com.daniel.ddd.payment.model.dto.request.PayCancelRequestDto;

import java.util.List;

/**
 * 카카오페이 서비스 인터페이스
 */
public interface KakaoPaymentApplicationService {

    KakaoPayResponse.OrderInfoResponse getOrderInfo(String franchiseeId, String payId, String requestUrl);

    KakaoPayResponse.PayReadyResponse payItems(List<ItemPayRequestDto> itemPayRequestDtos, String requestUrl, String loginId);

    KakaoPayResponse.PayApproveResponse completePayment(String payToken, String loginId);

    KakaoPayResponse.PayCancelResponse cancelPayment(PayCancelRequestDto payCancelRequestDto);

}
