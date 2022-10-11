package com.daniel.mychickenbreastshop.domain.payment.model.query;

import com.daniel.mychickenbreastshop.domain.payment.model.Payment;

import java.util.Optional;

public interface PaymentCustomQueryRepository {

    Optional<Payment> findByIdWithFetchJoin(Long paymentId);
}
