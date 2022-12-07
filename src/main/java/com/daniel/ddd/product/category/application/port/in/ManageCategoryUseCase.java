package com.daniel.ddd.product.category.application.port.in;

import com.daniel.ddd.product.category.model.dto.request.ModifyRequestDto;
import com.daniel.ddd.product.category.model.dto.request.RegisterRequestDto;

public interface ManageCategoryUseCase {

    Long registerCategory(RegisterRequestDto registerRequestDto);

    void modifyCategory(Long categoryId, ModifyRequestDto modifyRequestDto);
}
