package com.daniel.mychickenbreastshop.product.category.application.service;

import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import com.daniel.mychickenbreastshop.product.category.adaptor.out.persistence.CategoryRepository;
import com.daniel.mychickenbreastshop.product.category.application.port.in.ManageCategoryUseCase;
import com.daniel.mychickenbreastshop.product.category.domain.Category;
import com.daniel.mychickenbreastshop.product.category.model.dto.request.ModifyRequestDto;
import com.daniel.mychickenbreastshop.product.category.model.dto.request.RegisterRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.daniel.mychickenbreastshop.product.category.domain.enums.ErrorMessages.CATEGORY_NOT_EXISTS;

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
