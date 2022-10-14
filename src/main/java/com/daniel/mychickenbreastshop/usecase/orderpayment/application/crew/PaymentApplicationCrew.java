package com.daniel.mychickenbreastshop.usecase.orderpayment.application.crew;

import com.daniel.mychickenbreastshop.domain.payment.application.CommonPaymentService;
import com.daniel.mychickenbreastshop.domain.payment.model.dto.response.PaymentInfoResponseDto;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.strategy.PaymentStrategyFactory;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.model.PaymentResult;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.strategy.service.PaymentStrategyApplication;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.validate.ItemValidator;
import com.daniel.mychickenbreastshop.usecase.orderpayment.model.dto.request.ItemPayRequestDto;
import com.daniel.mychickenbreastshop.usecase.orderpayment.model.dto.request.PayCancelRequestDto;
import com.daniel.mychickenbreastshop.usecase.orderpayment.model.enums.PaymentGateway;
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
    private final CommonPaymentService commonPaymentService;
    private final ItemValidator itemValidator;

    public String getSingleItemPayResultUrl(ItemPayRequestDto itemPayRequestDto, String requestUrl, String loginId, PaymentGateway paymentGateway) {
        itemValidator.itemValidate(itemPayRequestDto);
        PaymentStrategyApplication<PaymentResult> paymentApplication = getPaymentStrategyApplication(paymentGateway);
        return paymentApplication.payItem(itemPayRequestDto, requestUrl, loginId).getRedirectUrl();
    }

    public String getCartItemsPayUrl(String cookieValue, String requestUrl, String loginId, PaymentGateway paymentGateway) {
        PaymentStrategyApplication<PaymentResult> paymentApplication = getPaymentStrategyApplication(paymentGateway);
        return paymentApplication.payCart(cookieValue, requestUrl, loginId).getRedirectUrl();
    }

/*    // 애매한 놈
*//*    public ApiPayInfoDto getApiPaymentDetail(String franchiseeId, String payId, PaymentGateway paymentGateway) {
        PaymentStrategyApplication<PaymentResult> paymentapplication = getPaymentStrategyApplication(paymentGateway);
        return null;
    }*/

    public void approvePayment(String payToken, String loginId, PaymentGateway paymentGateway) {
        PaymentStrategyApplication<PaymentResult> paymentApplication = getPaymentStrategyApplication(paymentGateway);
        paymentApplication.completePayment(payToken, loginId);
    }

    public void cancelPayment(PayCancelRequestDto payCancelRequestDto, PaymentGateway paymentGateway, String loginId) {
        PaymentStrategyApplication<PaymentResult> paymentApplication = getPaymentStrategyApplication(paymentGateway);
        paymentApplication.cancelPayment(payCancelRequestDto, loginId);
    }

    private PaymentStrategyApplication<PaymentResult> getPaymentStrategyApplication(PaymentGateway paymentGateway) {
        return paymentStrategyFactory.findStrategy(paymentGateway);
    }

    public PaymentInfoResponseDto getPaymentDetail(Long paymentId) {
        return commonPaymentService.getPaymentDetail(paymentId);
    }
}
