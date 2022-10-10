package com.daniel.mychickenbreastshop.domain.payment.domain.pay.query;

import com.daniel.mychickenbreastshop.domain.payment.domain.pay.Payment;

import java.util.Optional;

public interface PaymentCustomQueryRepository {

    Optional<Payment> findByIdWithFetchJoin(Long paymentId);
}
