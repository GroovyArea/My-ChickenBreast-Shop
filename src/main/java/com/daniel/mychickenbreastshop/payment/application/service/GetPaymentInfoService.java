package com.daniel.mychickenbreastshop.payment.application.service;

import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import com.daniel.mychickenbreastshop.payment.adaptor.out.persistence.PaymentRepository;
import com.daniel.mychickenbreastshop.payment.application.port.in.GetPaymentInfoUseCase;
import com.daniel.mychickenbreastshop.payment.domain.Payment;
import com.daniel.mychickenbreastshop.payment.mapper.PaymentInfoMapper;
import com.daniel.mychickenbreastshop.payment.model.dto.response.PaymentInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.daniel.mychickenbreastshop.payment.domain.enums.ErrorMessages.PAYMENT_NOT_EXISTS;

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
