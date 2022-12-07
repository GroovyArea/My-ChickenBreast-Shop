package com.daniel.ddd.payment.application.service.strategy;

import com.daniel.ddd.global.error.exception.BadRequestException;
import com.daniel.ddd.payment.application.service.gateway.model.PaymentResult;
import com.daniel.ddd.payment.model.enums.PaymentGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.daniel.ddd.payment.domain.enums.ErrorMessages.UNCORRECTED_API;

/**
 * Payment 전략 제공 팩토리 클래스
 * Payment Strategy Application 제공
 */
@Component
public class PaymentStrategyFactory {

    private Map<PaymentGateway, PaymentStrategyService<PaymentResult>> strategies;

    @Autowired
    public PaymentStrategyFactory(Set<PaymentStrategyService<PaymentResult>> services) {
        createStrategy(services);
    }

    public PaymentStrategyService<PaymentResult> findStrategy(PaymentGateway paymentGateway) {
        if (!Arrays.asList(PaymentGateway.values()).contains(paymentGateway)) {
            throw new BadRequestException(UNCORRECTED_API.getMessage());
        }

        return strategies.get(paymentGateway);
    }

    private void createStrategy(Set<PaymentStrategyService<PaymentResult>> services) {
        strategies = new ConcurrentHashMap<>();
        services.forEach(
                strategy -> strategies.put(strategy.getPaymentGatewayName(), strategy)
        );
    }
}
