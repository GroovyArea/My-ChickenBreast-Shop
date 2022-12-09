package com.daniel.mychickenbreastshop.product.category.application.port.in;

import com.daniel.mychickenbreastshop.product.category.model.dto.request.ModifyRequestDto;
import com.daniel.mychickenbreastshop.product.category.model.dto.request.RegisterRequestDto;

public interface ManageCategoryUseCase {

    Long registerCategory(RegisterRequestDto registerRequestDto);

    void modifyCategory(Long categoryId, ModifyRequestDto modifyRequestDto);
}
