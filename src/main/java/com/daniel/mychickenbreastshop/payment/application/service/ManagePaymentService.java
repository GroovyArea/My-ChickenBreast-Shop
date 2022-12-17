package com.daniel.mychickenbreastshop.payment.application.service;

import com.daniel.mychickenbreastshop.payment.application.port.in.ManagePaymentUseCase;
import com.daniel.mychickenbreastshop.payment.application.service.gateway.model.PaymentResult;
import com.daniel.mychickenbreastshop.payment.application.service.gateway.model.enums.PaymentGateway;
import com.daniel.mychickenbreastshop.payment.application.service.strategy.PaymentStrategyFactory;
import com.daniel.mychickenbreastshop.payment.application.service.strategy.PaymentStrategyService;
import com.daniel.mychickenbreastshop.payment.model.dto.request.ItemPayRequestDto;
import com.daniel.mychickenbreastshop.payment.model.dto.request.PayCancelRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagePaymentService implements ManagePaymentUseCase {

    private final PaymentStrategyFactory paymentStrategyFactory;

    @Override
    public PaymentResult createPaymentReady(List<ItemPayRequestDto> itemPayRequestDtos, PaymentGateway paymentGateway,
                                            String requestUrl, String loginId, Long orderId) {
        PaymentStrategyService<PaymentResult> paymentStrategyService = getPaymentStrategyApplication(paymentGateway);
        return paymentStrategyService.payItems(itemPayRequestDtos, requestUrl, loginId, orderId);
    }

    @Override
    public void approvePayment(PaymentGateway paymentGateway, String payToken, Long paymentId, String loginId) {
        PaymentStrategyService<PaymentResult> paymentStrategyService = getPaymentStrategyApplication(paymentGateway);
        paymentStrategyService.completePayment(payToken, paymentId, loginId);
    }

    @Override
    public void cancelPayment(PaymentGateway paymentGateway, PayCancelRequestDto payCancelRequestDto, Long paymentId, String loginId) {
        PaymentStrategyService<PaymentResult> paymentStrategyService = getPaymentStrategyApplication(paymentGateway);
        paymentStrategyService.cancelPayment(payCancelRequestDto, paymentId, loginId);
    }

    private PaymentStrategyService<PaymentResult> getPaymentStrategyApplication(PaymentGateway paymentGateway) {
        return paymentStrategyFactory.findStrategy(paymentGateway);
    }
}
