package com.daniel.mychickenbreastshop.domain.order.repository;

import com.daniel.mychickenbreastshop.domain.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
