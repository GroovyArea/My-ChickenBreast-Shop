package com.daniel.mychickenbreastshop.product.item.adaptor.out.persistence;

import com.daniel.mychickenbreastshop.product.item.adaptor.out.persistence.query.ItemCustomQueryRepository;
import com.daniel.mychickenbreastshop.product.item.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ItemCustomQueryRepository {

    Optional<Product> findByName(String productName);
}
