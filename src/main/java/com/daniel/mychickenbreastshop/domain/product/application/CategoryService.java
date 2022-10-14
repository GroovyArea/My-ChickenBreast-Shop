package com.daniel.mychickenbreastshop.domain.product.application;

import com.daniel.mychickenbreastshop.domain.product.model.category.Category;
import com.daniel.mychickenbreastshop.domain.product.model.category.CategoryRepository;
import com.daniel.mychickenbreastshop.domain.product.model.category.dto.request.ModifyRequestDto;
import com.daniel.mychickenbreastshop.domain.product.model.category.dto.request.RegisterRequestDto;
import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.daniel.mychickenbreastshop.domain.product.model.category.enums.CategoryResponse.CATEGORY_NOT_EXISTS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public void registerCategory(RegisterRequestDto registerRequestDto) {
        categoryRepository.save(Category.builder().categoryName(registerRequestDto.getCategoryName()).build());
    }

    @Transactional
    public void modifyCategory(Long categoryId, ModifyRequestDto modifyRequestDto) {
        Category savedCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new BadRequestException(CATEGORY_NOT_EXISTS.getMessage()));
        savedCategory.updateCategoryName(modifyRequestDto.getCategoryName());
    }
}
