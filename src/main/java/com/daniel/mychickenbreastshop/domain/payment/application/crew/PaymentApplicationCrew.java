package com.daniel.mychickenbreastshop.domain.payment.application.crew;

import com.daniel.mychickenbreastshop.domain.payment.application.payment.PaymentStrategyFactory;
import com.daniel.mychickenbreastshop.domain.payment.application.payment.strategy.service.PaymentStrategyApplication;
import com.daniel.mychickenbreastshop.domain.payment.application.payment.strategy.model.PaymentResult;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.request.ItemPayRequestDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.request.PayCancelRequestDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.model.PaymentApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 결제 API Enum을 전달 받음
 * 맞춤 결제 전략을 제공 및 메서드 호출
 */
@Service
@RequiredArgsConstructor
public class PaymentApplicationCrew {

    private final PaymentStrategyFactory paymentStrategyFactory;

    /**
     * api에서 요청하는 메서드들 만들기 payservice 참고
     * 트랜잭션 어디서 처리할지 고민해보자
     * 클래스 이름도
     */

    public String getSingleItemPayResultUrl(ItemPayRequestDto itemPayRequestDto, String requestUrl, String loginId, PaymentApi paymentApi) {
        PaymentStrategyApplication<PaymentResult> paymentApplication = getPaymentStrategyApplication(paymentApi);
        return paymentApplication.payItem(itemPayRequestDto, requestUrl, loginId).getRedirectUrl();
    }

    public String getCartItemsPayUrl(String cookieValue, String requestUrl, String loginId, PaymentApi paymentApi) {
        PaymentStrategyApplication<PaymentResult> paymentApplication = getPaymentStrategyApplication(paymentApi);
        return paymentApplication.payCart(cookieValue, requestUrl, loginId).getRedirectUrl();
    }

    // 애매한 놈
/*    public ApiPayInfoDto getApiPaymentDetail(String franchiseeId, String payId, PaymentApi paymentApi) {
        PaymentStrategyApplication<PaymentResult> paymentapplication = getPaymentStrategyApplication(paymentApi);
        return null;
    }*/

    public void approvePayment(String payToken, String loginId, PaymentApi paymentApi) {
        PaymentStrategyApplication<PaymentResult> paymentApplication = getPaymentStrategyApplication(paymentApi);
        paymentApplication.completePayment(payToken, loginId);
    }

    public void cancelPayment(PayCancelRequestDto payCancelRequestDto, PaymentApi paymentApi) {
        PaymentStrategyApplication<PaymentResult> paymentApplication = getPaymentStrategyApplication(paymentApi);
        paymentApplication.cancelPayment(payCancelRequestDto);
    }

    private PaymentStrategyApplication<PaymentResult> getPaymentStrategyApplication(PaymentApi paymentApi) {
        return paymentStrategyFactory.findStrategy(paymentApi);
    }
}
