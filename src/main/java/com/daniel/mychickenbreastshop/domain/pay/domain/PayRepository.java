package com.daniel.mychickenbreastshop.domain.pay.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PayRepository extends JpaRepository<Payment, Long> {
}
