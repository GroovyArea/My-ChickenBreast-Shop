package com.daniel.mychickenbreastshop.domain.product.category.model;

import com.daniel.mychickenbreastshop.domain.product.category.model.enums.ChickenCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCategoryName(ChickenCategory categoryName);

}
