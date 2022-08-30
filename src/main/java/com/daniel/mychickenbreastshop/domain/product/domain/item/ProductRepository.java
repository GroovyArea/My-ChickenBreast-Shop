package com.daniel.mychickenbreastshop.domain.product.domain.item;

import com.daniel.mychickenbreastshop.domain.product.domain.category.ChickenCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT p" +
            " FROM Product p" +
            " JOIN p.category c" +
            " WHERE c.categoryName = :categoryName")
    List<Product> findByJoinCategory(@Param("categoryName") ChickenCategory categoryName, Pageable pageable);
}
