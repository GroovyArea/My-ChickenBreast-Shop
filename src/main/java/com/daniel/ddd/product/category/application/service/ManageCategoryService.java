package com.daniel.ddd.product.category.application.service;

import com.daniel.ddd.product.category.adaptor.out.persistence.CategoryRepository;
import com.daniel.ddd.product.category.application.port.in.ManageCategoryUseCase;
import com.daniel.ddd.product.category.model.dto.request.ModifyRequestDto;
import com.daniel.ddd.product.category.model.dto.request.RegisterRequestDto;
import com.daniel.mychickenbreastshop.domain.product.category.model.Category;
import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.daniel.mychickenbreastshop.domain.product.category.model.enums.ErrorMessages.CATEGORY_NOT_EXISTS;

@Service
@RequiredArgsConstructor
public class ManageCategoryService implements ManageCategoryUseCase {

    private final CategoryRepository categoryRepository;

    @Override
    public Long registerCategory(RegisterRequestDto registerRequestDto) {
        return categoryRepository.save(
                Category.builder()
                        .categoryName(registerRequestDto.getCategoryName())
                        .build()
        ).getId();
    }

    @Override
    public void modifyCategory(Long categoryId, ModifyRequestDto modifyRequestDto) {
        Category savedCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BadRequestException(CATEGORY_NOT_EXISTS.getMessage()));
        savedCategory.updateCategoryName(modifyRequestDto.getCategoryName());
    }
}
