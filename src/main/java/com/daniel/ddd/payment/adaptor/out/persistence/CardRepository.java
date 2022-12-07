package com.daniel.ddd.payment.adaptor.out.persistence;

import com.daniel.ddd.payment.domain.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
}
