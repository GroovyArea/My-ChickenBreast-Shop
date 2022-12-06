package com.daniel.ddd.product.item.adaptor.out.persistence;

import com.daniel.mychickenbreastshop.domain.product.item.model.Product;
import com.daniel.mychickenbreastshop.domain.product.item.model.query.ItemCustomQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ItemCustomQueryRepository {

    Optional<Product> findByName(String productName);
}
