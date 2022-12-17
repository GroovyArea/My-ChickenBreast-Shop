package com.daniel.mychickenbreastshop.payment.application.port.in;

import com.daniel.mychickenbreastshop.payment.model.dto.response.PaymentInfoResponseDto;

public interface GetPaymentInfoUseCase {

    PaymentInfoResponseDto getPaymentDetail(Long paymentId);
}
