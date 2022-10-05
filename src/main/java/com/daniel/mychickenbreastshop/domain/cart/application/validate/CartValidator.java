package com.daniel.mychickenbreastshop.domain.cart.application.validate;

import com.daniel.mychickenbreastshop.domain.cart.domain.dto.request.CartRequestDto;
import com.daniel.mychickenbreastshop.domain.product.application.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartValidator {

    private final ProductService productService;

    public void cartValidate(CartRequestDto cartRequestDto) {
        productService.validatePayAmount(cartRequestDto.getItemNo(), cartRequestDto.getTotalPrice(),
                cartRequestDto.getItemQuantity());
    }
}
