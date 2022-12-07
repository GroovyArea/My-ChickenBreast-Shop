package com.daniel.mychickenbreastshop.usecase.orderpayment.application.strategy;

import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.model.PaymentResult;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.strategy.service.PaymentStrategyApplication;
import com.daniel.mychickenbreastshop.usecase.orderpayment.model.enums.PaymentGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.daniel.ddd.payment.domain.enums.ErrorMessages.*;

/**
 * Payment 전략 제공 팩토리 클래스
 * Payment Strategy Application 제공
 */
@Component
public class PaymentStrategyFactory {

    private Map<PaymentGateway, PaymentStrategyApplication<PaymentResult>> strategies;

    @Autowired
    public PaymentStrategyFactory(Set<PaymentStrategyApplication<PaymentResult>> services) {
        createStrategy(services);
    }

    public PaymentStrategyApplication<PaymentResult> findStrategy(PaymentGateway paymentGateway) {
        if (!Arrays.asList(PaymentGateway.values()).contains(paymentGateway)) {
            throw new BadRequestException(UNCORRECTED_API.getMessage());
        }

        return strategies.get(paymentGateway);
    }

    private void createStrategy(Set<PaymentStrategyApplication<PaymentResult>> services) {
        strategies = new HashMap<>();
        services.forEach(
                strategy -> strategies.put(strategy.getPaymentGatewayName(), strategy)
        );
    }
}
