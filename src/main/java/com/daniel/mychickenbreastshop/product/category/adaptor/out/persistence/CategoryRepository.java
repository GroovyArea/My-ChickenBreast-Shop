package com.daniel.mychickenbreastshop.product.category.adaptor.out.persistence;


import com.daniel.mychickenbreastshop.product.category.domain.Category;
import com.daniel.mychickenbreastshop.product.category.domain.enums.ChickenCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCategoryName(ChickenCategory categoryName);

}
