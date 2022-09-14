package com.daniel.mychickenbreastshop.domain.payment.application.payment;

import com.daniel.mychickenbreastshop.domain.payment.domain.pay.model.PaymentApi;
import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.daniel.mychickenbreastshop.domain.payment.domain.pay.model.PaymentResponse.UNCORRECTED_API;

@Component
public class PayApplicantFactory {

    private Map<PaymentApi, PaymentApplicant> strategies;

    @Autowired
    public PayApplicantFactory(Set<PaymentApplicant> applicants) {
        createStrategy(applicants);
    }

    public PaymentApplicant findStrategy(PaymentApi paymentApi) {
        if (!Arrays.asList(PaymentApi.values()).contains(paymentApi)) {
            throw new BadRequestException(UNCORRECTED_API.getMessage());
        }
        return strategies.get(paymentApi);
    }

    private void createStrategy(Set<PaymentApplicant> applicants) {
        strategies = new HashMap<>();
        applicants.forEach(
                strategy -> strategies.put(strategy.getPaymentApiName(), strategy)
        );
    }
}
