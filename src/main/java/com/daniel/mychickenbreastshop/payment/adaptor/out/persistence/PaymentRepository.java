package com.daniel.mychickenbreastshop.payment.adaptor.out.persistence;

import com.daniel.mychickenbreastshop.payment.domain.Payment;
import com.daniel.mychickenbreastshop.payment.adaptor.out.persistence.query.PaymentCustomQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long>, PaymentCustomQueryRepository {
}
