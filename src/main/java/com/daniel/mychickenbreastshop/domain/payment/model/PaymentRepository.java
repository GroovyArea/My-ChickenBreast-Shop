package com.daniel.mychickenbreastshop.domain.payment.model;

import com.daniel.mychickenbreastshop.domain.payment.model.query.PaymentCustomQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long>, PaymentCustomQueryRepository {
}
