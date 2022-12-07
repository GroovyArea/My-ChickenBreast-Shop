package com.daniel.ddd.payment.adaptor.out.persistence.query;

import com.daniel.ddd.payment.domain.Payment;

import java.util.Optional;

public interface PaymentCustomQueryRepository {

    Optional<Payment> findByIdWithCardUsingFetchJoin(Long paymentId);
}
