package com.daniel.mychickenbreastshop.domain.payment.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderedProductRepository extends JpaRepository<OrderedProduct, Long> {
}
