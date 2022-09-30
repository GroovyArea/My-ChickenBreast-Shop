package com.daniel.mychickenbreastshop.domain.payment.domain.pay;

import com.daniel.mychickenbreastshop.domain.payment.domain.pay.query.PaymentCustomQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long>, PaymentCustomQueryRepository {
}
