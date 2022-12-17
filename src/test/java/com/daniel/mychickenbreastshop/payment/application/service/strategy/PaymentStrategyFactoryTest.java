package com.daniel.mychickenbreastshop.payment.application.service.strategy;

import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import com.daniel.mychickenbreastshop.global.event.builder.EventBuilder;
import com.daniel.mychickenbreastshop.global.event.model.DomainEvent;
import com.daniel.mychickenbreastshop.payment.adaptor.out.persistence.PaymentRepository;
import com.daniel.mychickenbreastshop.payment.application.service.gateway.kakaopay.application.KakaoPaymentApplicationService;
import com.daniel.mychickenbreastshop.payment.application.service.gateway.model.PaymentResult;
import com.daniel.mychickenbreastshop.payment.application.service.gateway.model.enums.PaymentGateway;
import com.daniel.mychickenbreastshop.payment.application.service.strategy.kakaopay.KakaopayStrategyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PaymentStrategyFactoryTest {

    private PaymentStrategyFactory paymentStrategyFactory;

    @Mock
    KakaoPaymentApplicationService kakaoPaymentApplicationService;

    @Mock
    ApplicationEventPublisher eventPublisher;

    @Mock
    EventBuilder<DomainEvent> eventBuilder;

    @Mock
    PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        Set<PaymentStrategyService<PaymentResult>> strategyApplications = Set.of(new KakaopayStrategyService(
                kakaoPaymentApplicationService, eventPublisher,
                eventBuilder, paymentRepository));

        paymentStrategyFactory = new PaymentStrategyFactory(strategyApplications);
    }

    @DisplayName("주어진 API Gateway에 맞는 결제 전략 서비스 객체를 반환한다.")
    @Test
    void findStrategy() {
        // given
        PaymentGateway paymentGateway = PaymentGateway.KAKAO; // 카카오 페이 서비스 전략

        // when
        PaymentStrategyService<PaymentResult> strategy = paymentStrategyFactory.findStrategy(paymentGateway);

        assertThat(strategy.getPaymentGatewayName()).isEqualTo(paymentGateway);
    }

    @DisplayName("지원하지 않는 enum 값을 넘겼을 경우 예외를 발생시킨다.")
    @Test
    void findStrategyException() {
        assertThatThrownBy(() -> paymentStrategyFactory.findStrategy(null))
                .isInstanceOf(BadRequestException.class).hasMessage("해당 결제 API를 이용할 수 없습니다.");
    }
}