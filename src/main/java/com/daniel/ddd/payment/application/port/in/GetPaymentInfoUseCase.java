package com.daniel.ddd.payment.application.port.in;

import com.daniel.ddd.payment.model.dto.response.PaymentInfoResponseDto;

public interface GetPaymentInfoUseCase {

    PaymentInfoResponseDto getPaymentDetail(Long paymentId);
}
