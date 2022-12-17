package com.daniel.mychickenbreastshop.payment.adaptor.out.persistence.query;

import com.daniel.mychickenbreastshop.payment.domain.Payment;

import java.util.Optional;

public interface PaymentCustomQueryRepository {

    Optional<Payment> findByIdWithCardUsingFetchJoin(Long paymentId);
}
