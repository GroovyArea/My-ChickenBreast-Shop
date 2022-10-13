package com.daniel.mychickenbreastshop.usecase.orderpayment.application.validate;

import com.daniel.mychickenbreastshop.domain.product.application.ProductService;
import com.daniel.mychickenbreastshop.usecase.orderpayment.model.dto.request.ItemPayRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemValidator {

    private final ProductService productService;

    public void itemValidate(ItemPayRequestDto itemPayRequestDto) {
        productService.validatePayAmount(itemPayRequestDto.getItemNumber(), itemPayRequestDto.getQuantity(),
                itemPayRequestDto.getTotalAmount());
    }
}
