package com.daniel.ddd.payment.application.port.in;

import com.daniel.ddd.payment.application.service.gateway.model.PaymentResult;
import com.daniel.ddd.payment.model.dto.request.ItemPayRequestDto;
import com.daniel.ddd.payment.model.dto.request.PayCancelRequestDto;
import com.daniel.ddd.payment.model.enums.PaymentGateway;

import java.util.List;

public interface ManagePaymentUseCase {

    PaymentResult createPaymentReady(
            List<ItemPayRequestDto> itemPayRequestDtos,
            PaymentGateway paymentGateway,
            String requestUrl,
            String loginId,
            Long orderId
    );

    void approvePayment(PaymentGateway paymentGateway,
                        String payToken,
                        Long paymentId,
                        String loginId);

    void cancelPayment(PaymentGateway paymentGateway,
                       PayCancelRequestDto payCancelRequestDto,
                       Long paymentId,
                       String loginId);
}
