package com.daniel.mychickenbreastshop.payment.adaptor.out.persistence.query;

import com.daniel.mychickenbreastshop.global.config.QuerydslConfig;
import com.daniel.mychickenbreastshop.payment.adaptor.out.persistence.PaymentRepository;
import com.daniel.mychickenbreastshop.payment.domain.Card;
import com.daniel.mychickenbreastshop.payment.domain.Payment;
import com.daniel.mychickenbreastshop.payment.domain.enums.PayStatus;
import com.daniel.mychickenbreastshop.payment.domain.enums.PaymentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
class PaymentCustomQueryRepositoryImplTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        for (int i = 0; i < 10; i++) {
            Card card = Card.builder()
                    .bin("bin")
                    .cardType("HANA")
                    .build();

            Payment payment = Payment.builder()
                    .orderId((long) i)
                    .totalPrice(100000L)
                    .paymentType(PaymentType.CASH)
                    .status(PayStatus.COMPLETED)
                    .card(card)
                    .build();

            paymentRepository.save(payment);
        }
    }

    @DisplayName("결제 번호를 통해 카드 정보와 Fetch Join 한 결과를 반환한다.")
    @Test
    void findByIdWithFetchJoin() {
        // given
        Long paymentId = 1L;

        // when
        Payment savedPayment = paymentRepository.findByIdWithCardUsingFetchJoin(paymentId).
                orElseThrow(() -> new RuntimeException("payment empty"));

        assertThat(savedPayment.getPaymentType()).isEqualTo(PaymentType.CASH);
        assertThat(savedPayment.getCard().getBin()).isEqualTo("bin");
        assertThat(savedPayment.getCard().getCardType()).isEqualTo("HANA");
    }
}