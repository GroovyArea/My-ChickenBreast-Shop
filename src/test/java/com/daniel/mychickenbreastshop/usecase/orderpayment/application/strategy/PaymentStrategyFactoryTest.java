package com.daniel.mychickenbreastshop.usecase.orderpayment.application.strategy;

import com.daniel.mychickenbreastshop.domain.product.item.model.ProductRepository;
import com.daniel.mychickenbreastshop.domain.user.model.UserRepository;
import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import com.daniel.mychickenbreastshop.order.adaptor.out.persistence.OrderRepository;
import com.daniel.mychickenbreastshop.payment.application.service.strategy.PaymentStrategyFactory;
import com.daniel.mychickenbreastshop.payment.model.enums.PaymentGateway;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.application.KakaoPaymentService;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.model.PaymentResult;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.strategy.service.PaymentStrategyApplication;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.strategy.service.adjust.ItemQuantityAdjuster;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.strategy.service.kakaopay.KakaopayStrategyApplication;
import com.daniel.mychickenbreastshop.usecase.orderpayment.extract.CartDisassembler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class PaymentStrategyFactoryTest {

    private PaymentStrategyFactory paymentStrategyFactory;

    @Mock
    KakaoPaymentService kakaoPaymentService;

    @Mock
    CartDisassembler cartDisassembler;

    @Mock
    ItemQuantityAdjuster kakaopayItemQuantityAdjuster;

    @Mock
    UserRepository userRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        Set<PaymentStrategyApplication<PaymentResult>> strategyApplications = Set.of(new KakaopayStrategyApplication(kakaoPaymentService, cartDisassembler, kakaopayItemQuantityAdjuster, userRepository, productRepository, orderRepository));
        paymentStrategyFactory = new PaymentStrategyFactory(strategyApplications);
    }

    @DisplayName("주어진 API Gateway에 맞는 결제 전략 서비스 객체를 반환한다.")
    @Test
    void findStrategy() {
        // given
        PaymentGateway paymentGateway = PaymentGateway.KAKAO; // 카카오 페이 서비스 전략

        // when
        PaymentStrategyApplication<PaymentResult> strategy = paymentStrategyFactory.findStrategy(paymentGateway);

        assertThat(strategy.getPaymentGatewayName()).isEqualTo(paymentGateway);
    }

    @DisplayName("지원하지 않는 enum 값을 넘겼을 경우 예외를 발생시킨다.")
    @Test
    void findStrategyException() {
        assertThatThrownBy(() -> paymentStrategyFactory.findStrategy(null))
                .isInstanceOf(BadRequestException.class).hasMessage("해당 결제 API를 이용할 수 없습니다.");
    }
}