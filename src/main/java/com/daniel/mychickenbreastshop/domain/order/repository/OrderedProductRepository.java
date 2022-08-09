package com.daniel.mychickenbreastshop.domain.order.repository;

import com.daniel.mychickenbreastshop.domain.order.domain.OrderedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderedProductRepository extends JpaRepository<OrderedProduct, Long> {
}
