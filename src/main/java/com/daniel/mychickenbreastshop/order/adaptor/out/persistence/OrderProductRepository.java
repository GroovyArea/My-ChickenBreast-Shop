package com.daniel.mychickenbreastshop.order.adaptor.out.persistence;

import com.daniel.mychickenbreastshop.order.domain.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
