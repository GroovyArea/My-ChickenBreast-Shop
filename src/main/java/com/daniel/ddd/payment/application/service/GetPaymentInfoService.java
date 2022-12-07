package com.daniel.ddd.payment.application.service;

import com.daniel.ddd.global.error.exception.BadRequestException;
import com.daniel.ddd.payment.adaptor.out.persistence.PaymentRepository;
import com.daniel.ddd.payment.application.port.in.GetPaymentInfoUseCase;
import com.daniel.ddd.payment.domain.Payment;
import com.daniel.ddd.payment.mapper.PaymentInfoMapper;
import com.daniel.ddd.payment.model.dto.response.PaymentInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.daniel.ddd.payment.domain.enums.ErrorMessages.PAYMENT_NOT_EXISTS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetPaymentInfoService implements GetPaymentInfoUseCase {

    private final PaymentRepository paymentRepository;
    private final PaymentInfoMapper paymentInfoMapper;

    @Override
    public PaymentInfoResponseDto getPaymentDetail(Long paymentId) {
        Payment payment = paymentRepository.findByIdWithCardUsingFetchJoin(paymentId).orElseThrow(() -> new BadRequestException(PAYMENT_NOT_EXISTS.getMessage()));
        return paymentInfoMapper.toDTO(payment);
    }
}
