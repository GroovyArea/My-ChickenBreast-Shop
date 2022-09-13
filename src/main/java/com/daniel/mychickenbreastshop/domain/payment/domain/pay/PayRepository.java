package com.daniel.mychickenbreastshop.domain.payment.domain.pay;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PayRepository extends JpaRepository<Payment, Long> {
}
