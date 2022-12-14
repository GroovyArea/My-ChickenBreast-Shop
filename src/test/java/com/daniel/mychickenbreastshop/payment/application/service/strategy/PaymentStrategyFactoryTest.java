package com.daniel.mychickenbreastshop.payment.application.service.strategy;

import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import com.daniel.mychickenbreastshop.global.event.builder.EventBuilder;
import com.daniel.mychickenbreastshop.payment.adaptor.out.persistence.PaymentRepository;
import com.daniel.mychickenbreastshop.payment.application.port.out.event.model.ItemsVariation;
import com.daniel.mychickenbreastshop.payment.application.port.out.event.model.OrderVariation;
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

@ExtendWith(MockitoExtension.class)
class PaymentStrategyFactoryTest {

    private PaymentStrategyFactory paymentStrategyFactory;

    @Mock
    KakaoPaymentApplicationService kakaoPaymentApplicationService;

    @Mock
    ApplicationEventPublisher eventPublisher;

    @Mock
    private EventBuilder<ItemsVariation> itemsVariationEventBuilder;

    @Mock
    private EventBuilder<OrderVariation> orderVariationEventBuilder;

    @Mock
    PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        Set<PaymentStrategyService<PaymentResult>> strategyApplications = Set.of(new KakaopayStrategyService(
                kakaoPaymentApplicationService, eventPublisher,
                itemsVariationEventBuilder, orderVariationEventBuilder, paymentRepository));

        paymentStrategyFactory = new PaymentStrategyFactory(strategyApplications);
    }

    @DisplayName("????????? API Gateway??? ?????? ?????? ?????? ????????? ????????? ????????????.")
    @Test
    void findStrategy() {
        // given
        PaymentGateway paymentGateway = PaymentGateway.KAKAO; // ????????? ?????? ????????? ??????

        // when
        PaymentStrategyService<PaymentResult> strategy = paymentStrategyFactory.findStrategy(paymentGateway);

        assertThat(strategy.getPaymentGatewayName()).isEqualTo(paymentGateway);
    }

    @DisplayName("???????????? ?????? enum ?????? ????????? ?????? ????????? ???????????????.")
    @Test
    void findStrategyException() {
        assertThatThrownBy(() -> paymentStrategyFactory.findStrategy(null))
                .isInstanceOf(BadRequestException.class).hasMessage("?????? ?????? API??? ????????? ??? ????????????.");
    }
}