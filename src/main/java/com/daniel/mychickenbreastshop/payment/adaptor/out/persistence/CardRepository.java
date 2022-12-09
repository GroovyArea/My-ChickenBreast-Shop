package com.daniel.mychickenbreastshop.payment.adaptor.out.persistence;

import com.daniel.mychickenbreastshop.payment.domain.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
}
