package com.daniel.mychickenbreastshop.domain.product.domain.item;

import com.daniel.mychickenbreastshop.domain.product.domain.item.query.ItemCustomQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ItemCustomQueryRepository {

    Optional<Product> findByName(String productName);
}
