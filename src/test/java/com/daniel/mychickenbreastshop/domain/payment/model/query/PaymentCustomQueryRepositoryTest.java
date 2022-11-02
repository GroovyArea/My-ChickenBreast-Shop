package com.daniel.mychickenbreastshop.domain.payment.model.query;

import com.daniel.mychickenbreastshop.domain.payment.model.Card;
import com.daniel.mychickenbreastshop.domain.payment.model.Payment;
import com.daniel.mychickenbreastshop.domain.payment.model.PaymentRepository;
import com.daniel.mychickenbreastshop.domain.payment.model.enums.PayStatus;
import com.daniel.mychickenbreastshop.domain.payment.model.enums.PaymentType;
import com.daniel.mychickenbreastshop.global.config.QuerydslConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
class PaymentCustomQueryRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {

        for (int i = 1; i < 10; i++) {
            Card card = Card.builder()
                    .bin("bin")
                    .cardType("HANA")
                    .build();

            Payment payment = Payment.builder()
                    .totalPrice(100000L)
                    .paymentType(PaymentType.CASH)
                    .status(PayStatus.COMPLETED)
                    .build();

            payment.updateCardInfo(card);

            paymentRepository.save(payment);
        }
    }

    @DisplayName("결제 번호를 통해 카드 정보와 Fetch Join한 결과를 반환한다.")
    @Test
    void findByIdWithFetchJoin() {
        // given
        long paymentId = 1L;

        // when
        Payment savedPayment = paymentRepository.findByIdWithFetchJoin(paymentId).orElseThrow(() -> new RuntimeException("결제 정보가 존재하지 않음."));

        assertThat(savedPayment.getPaymentType()).isEqualTo(PaymentType.CASH);
        assertThat(savedPayment.getCard().getBin()).isEqualTo("bin");
        assertThat(savedPayment.getCard().getCardType()).isEqualTo("HANA");

    }
}