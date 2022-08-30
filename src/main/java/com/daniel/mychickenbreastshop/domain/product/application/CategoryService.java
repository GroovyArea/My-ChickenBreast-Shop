package com.daniel.mychickenbreastshop.domain.product.application;

import com.daniel.mychickenbreastshop.domain.product.domain.category.Category;
import com.daniel.mychickenbreastshop.domain.product.domain.category.CategoryRepository;
import com.daniel.mychickenbreastshop.domain.product.domain.category.dto.RegisterRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public void registerCategory(RegisterRequestDto registerRequestDto) {
        categoryRepository.save(Category.builder().categoryName(registerRequestDto.getCategoryName()).build());
    }

}
