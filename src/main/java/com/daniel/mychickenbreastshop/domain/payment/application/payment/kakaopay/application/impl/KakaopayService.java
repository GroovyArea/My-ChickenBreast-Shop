package com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.application.impl;

import com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.application.KakaoPaymentService;
import com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.webclient.model.KakaoPayResponse;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.request.ItemPayRequestDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.request.PayCancelRequestDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.response.kakaopay.ApiPayInfoDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.model.PaymentApi;
import org.springframework.stereotype.Service;

@Service
public class KakaopayService implements KakaoPaymentService {

    @Override
    public PaymentApi getPaymentApiName() {
        return null;
    }

    @Override
    public ApiPayInfoDto getOrderInfo(String franchiseeId, String payId) {
        return null;
    }

    @Override
    public KakaoPayResponse.PayReadyResponse payItem(ItemPayRequestDto itemPayRequestDto, String requestURL, String loginId) {
        return null;
    }

    @Override
    public KakaoPayResponse.PayReadyResponse payCart(String cookieValue, String requestURL, String loginId) {
        return null;
    }

    @Override
    public KakaoPayResponse.PayApproveResponse completePayment(String payToken, String loginId) {
        return null;
    }

    @Override
    public KakaoPayResponse.PayCancelResponse cancelPayment(PayCancelRequestDto payCancelRequestDto) {
        return null;
    }
}
