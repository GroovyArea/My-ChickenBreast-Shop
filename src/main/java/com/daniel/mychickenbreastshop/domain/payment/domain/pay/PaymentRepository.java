package com.daniel.mychickenbreastshop.domain.payment.domain.pay;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByPgToken(String pgToken);
}
