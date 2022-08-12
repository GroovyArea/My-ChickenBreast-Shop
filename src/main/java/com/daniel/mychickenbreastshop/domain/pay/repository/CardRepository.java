package com.daniel.mychickenbreastshop.domain.pay.repository;

import com.daniel.mychickenbreastshop.domain.pay.domain.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
}
