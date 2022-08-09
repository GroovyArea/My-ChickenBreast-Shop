package com.daniel.mychickenbreastshop.domain.pay.repository;

import com.daniel.mychickenbreastshop.domain.pay.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayRepository extends JpaRepository<Payment, Long> {
}
