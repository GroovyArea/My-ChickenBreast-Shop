package com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.application;

import com.daniel.mychickenbreastshop.domain.payment.application.payment.PaymentService;
import com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.webclient.model.KakaoPayResponse.OrderInfoResponse;
import com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.webclient.model.KakaoPayResponse.PayApproveResponse;
import com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.webclient.model.KakaoPayResponse.PayCancelResponse;
import com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.webclient.model.KakaoPayResponse.PayReadyResponse;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.request.ItemPayRequestDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.request.PayCancelRequestDto;

public interface KakaoPaymentService extends PaymentService {

    OrderInfoResponse getOrderInfo(String franchiseeId, String payId);

    PayReadyResponse payItem(ItemPayRequestDto itemPayRequestDto, String requestUrl, String loginId);

    PayReadyResponse payCart(String cookieValue, String requestUrl, String loginId);

    PayApproveResponse completePayment(String payToken, String loginId);

    PayCancelResponse cancelPayment(PayCancelRequestDto payCancelRequestDto);

}
