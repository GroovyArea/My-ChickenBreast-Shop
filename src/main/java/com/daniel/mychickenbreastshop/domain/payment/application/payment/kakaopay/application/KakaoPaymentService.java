package com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.application;

import com.daniel.mychickenbreastshop.domain.payment.application.payment.PaymentService;
import com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.webclient.model.KakaoPayResponse.PayApproveResponse;
import com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.webclient.model.KakaoPayResponse.PayCancelResponse;
import com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.webclient.model.KakaoPayResponse.PayReadyResponse;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.request.ItemPayRequestDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.request.PayCancelRequestDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.response.kakaopay.ApiPayInfoDto;

public interface KakaoPaymentService extends PaymentService {

    ApiPayInfoDto getOrderInfo(String franchiseeId, String payId);

    PayReadyResponse payItem(ItemPayRequestDto itemPayRequestDto, String requestURL, String loginId);

    PayReadyResponse payCart(String cookieValue, String requestURL, String loginId);

    PayApproveResponse completePayment(String payToken, String loginId);

    PayCancelResponse cancelPayment(PayCancelRequestDto payCancelRequestDto);


}
