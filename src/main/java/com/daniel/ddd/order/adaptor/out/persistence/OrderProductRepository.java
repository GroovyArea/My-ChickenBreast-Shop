package com.daniel.ddd.order.adaptor.out.persistence;

import com.daniel.ddd.order.domain.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
