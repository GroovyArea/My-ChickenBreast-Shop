package com.daniel.mychickenbreastshop.domain.payment.application;

import com.daniel.mychickenbreastshop.domain.payment.application.strategy.service.PaymentStrategyApplication;
import com.daniel.mychickenbreastshop.domain.payment.application.strategy.model.PaymentResult;
import com.daniel.mychickenbreastshop.domain.payment.model.model.PaymentApi;
import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.daniel.mychickenbreastshop.domain.payment.model.model.PaymentResponse.UNCORRECTED_API;

/**
 * Payment 전략 제공 팩토리 클래스
 * Payment Strategy Application 제공
 */
@Component
public class PaymentStrategyFactory {

    private Map<PaymentApi, PaymentStrategyApplication<PaymentResult>> strategies;

    @Autowired
    public PaymentStrategyFactory(Set<PaymentStrategyApplication<PaymentResult>> services) {
        createStrategy(services);
    }

    public PaymentStrategyApplication<PaymentResult> findStrategy(PaymentApi paymentApi) {
        if (!Arrays.asList(PaymentApi.values()).contains(paymentApi)) {
            throw new BadRequestException(UNCORRECTED_API.getMessage());
        }

        return strategies.get(paymentApi);
    }

    private void createStrategy(Set<PaymentStrategyApplication<PaymentResult>> services) {
        strategies = new HashMap<>();
        services.forEach(
                strategy -> strategies.put(strategy.getPaymentApiName(), strategy)
        );
    }
}
