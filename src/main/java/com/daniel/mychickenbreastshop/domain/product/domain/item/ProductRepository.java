package com.daniel.mychickenbreastshop.domain.product.domain.item;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT p" +
            " FROM Product p" +
            " JOIN p.category c" +
            " WHERE c.name = :categoryName")
    List<Product> findByJoinCategory(@Param("categoryName") String categoryName, Pageable pageable);
}
