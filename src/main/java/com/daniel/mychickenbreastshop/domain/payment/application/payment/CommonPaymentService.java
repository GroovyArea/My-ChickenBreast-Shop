package com.daniel.mychickenbreastshop.domain.payment.application.payment;

import com.daniel.mychickenbreastshop.domain.payment.domain.pay.Payment;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.PaymentRepository;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.response.PaymentInfoResponseDto;
import com.daniel.mychickenbreastshop.domain.payment.mapper.payment.PaymentInfoMapper;
import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.daniel.mychickenbreastshop.domain.payment.domain.pay.model.PaymentResponse.PAYMENT_NOT_EXISTS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommonPaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentInfoMapper paymentInfoMapper;

    public PaymentInfoResponseDto getPaymentDetail(Long paymentId) {
        Payment payment = paymentRepository.findByIdWithFetchJoin(paymentId).orElseThrow(() -> new BadRequestException(PAYMENT_NOT_EXISTS.getMessage()));
        return paymentInfoMapper.toDTO(payment);
    }
}
