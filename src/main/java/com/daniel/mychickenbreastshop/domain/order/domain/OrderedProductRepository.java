package com.daniel.mychickenbreastshop.domain.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderedProductRepository extends JpaRepository<OrderedProduct, Long> {
}
