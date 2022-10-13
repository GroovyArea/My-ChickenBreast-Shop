package com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.application;

import com.daniel.mychickenbreastshop.domain.payment.application.PaymentService;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient.model.KakaoPayResponse;
import com.daniel.mychickenbreastshop.usecase.orderpayment.extract.model.CartValue;
import com.daniel.mychickenbreastshop.usecase.orderpayment.model.dto.request.ItemPayRequestDto;
import com.daniel.mychickenbreastshop.usecase.orderpayment.model.dto.request.PayCancelRequestDto;

/**
 * 카카오페이 서비스 인터페이스
 */
public interface KakaoPaymentService extends PaymentService {

    KakaoPayResponse.OrderInfoResponse getOrderInfo(String franchiseeId, String payId);

    KakaoPayResponse.PayReadyResponse payItem(ItemPayRequestDto itemPayRequestDto, String requestUrl, String loginId);

    KakaoPayResponse.PayReadyResponse payCart(CartValue cartValue, String requestUrl, String loginId);

    KakaoPayResponse.PayApproveResponse completePayment(String payToken, String loginId);

    KakaoPayResponse.PayCancelResponse cancelPayment(PayCancelRequestDto payCancelRequestDto);

}
