package com.daniel.mychickenbreastshop.domain.product.repository;

import com.daniel.mychickenbreastshop.domain.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
