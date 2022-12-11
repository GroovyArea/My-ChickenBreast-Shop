package com.daniel.mychickenbreastshop.domain.payment.application;

import com.daniel.mychickenbreastshop.payment.application.service.GetPaymentInfoService;
import com.daniel.mychickenbreastshop.payment.mapper.PaymentInfoMapper;
import com.daniel.mychickenbreastshop.payment.domain.Card;
import com.daniel.mychickenbreastshop.payment.domain.Payment;
import com.daniel.mychickenbreastshop.payment.adaptor.out.persistence.PaymentRepository;
import com.daniel.mychickenbreastshop.payment.model.dto.response.PaymentInfoResponseDto;
import com.daniel.mychickenbreastshop.payment.domain.enums.PayStatus;
import com.daniel.mychickenbreastshop.payment.domain.enums.PaymentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetPaymentInfoServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentInfoMapper paymentInfoMapper;

    @InjectMocks
    private GetPaymentInfoService paymentInfoService;

    @DisplayName("결제 고유 id를 통해 상세 정보를 조회한다.")
    @Test
    void getPaymentDetail() {
        // given
        Card card = Card.builder()
                .id(1L)
                .bin("bin")
                .cardType("HANA")
                .installMonth("installMonth")
                .interestFreeInstall("interestFreeInstall")
                .build();

        Payment payment = Payment.builder()
                .id(1L)
                .totalPrice(1000L)
                .paymentType(PaymentType.CASH)
                .status(PayStatus.COMPLETED)
                .build();

        payment.updateCardInfo(card);

        PaymentInfoResponseDto dto = PaymentInfoResponseDto.builder()
                .paymentId(payment.getId())
                .totalPrice(payment.getTotalPrice())
                .payStatus(payment.getStatus())
                .paymentType(payment.getPaymentType())
                .cardId(card.getId())
                .cardType(card.getCardType())
                .installMonth(card.getInstallMonth())
                .interestFreeInstall(card.getInterestFreeInstall())
                .build();

        // when
        when(paymentRepository.findByIdWithCardUsingFetchJoin(payment.getId())).thenReturn(Optional.of(payment));
        when(paymentInfoMapper.toDTO(payment)).thenReturn(dto);

        assertThat(paymentInfoService.getPaymentDetail(payment.getId())).isEqualTo(dto);
    }
}