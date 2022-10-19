package com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.application;

import com.daniel.mychickenbreastshop.domain.payment.application.PaymentService;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient.model.KakaoPayResponse.PayApproveResponse;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient.model.KakaoPayResponse.PayReadyResponse;
import com.daniel.mychickenbreastshop.usecase.orderpayment.extract.model.CartValue;
import com.daniel.mychickenbreastshop.usecase.orderpayment.model.dto.request.ItemPayRequestDto;
import com.daniel.mychickenbreastshop.usecase.orderpayment.model.dto.request.PayCancelRequestDto;

import static com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient.model.KakaoPayResponse.OrderInfoResponse;
import static com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.webclient.model.KakaoPayResponse.PayCancelResponse;

/**
 * 카카오페이 서비스 인터페이스
 */
public interface KakaoPaymentService extends PaymentService {

    OrderInfoResponse getOrderInfo(String franchiseeId, String payId);

    PayReadyResponse payItem(ItemPayRequestDto itemPayRequestDto, String requestUrl, String loginId);

    PayReadyResponse payCart(CartValue cartValue, String requestUrl, String loginId);

    PayApproveResponse completePayment(String payToken, String loginId);

    PayCancelResponse cancelPayment(PayCancelRequestDto payCancelRequestDto);

}
