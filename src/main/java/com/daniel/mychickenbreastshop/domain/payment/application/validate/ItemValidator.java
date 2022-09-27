package com.daniel.mychickenbreastshop.domain.payment.application.validate;

import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.request.ItemPayRequestDto;
import com.daniel.mychickenbreastshop.domain.product.application.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemValidator {

    private final ProductService productService;

    public void itemValidate(ItemPayRequestDto itemPayRequestDto) {
        productService.validatePayAmount(itemPayRequestDto.getItemNumber(), itemPayRequestDto.getTotalAmount(),
                itemPayRequestDto.getQuantity());
    }
}
