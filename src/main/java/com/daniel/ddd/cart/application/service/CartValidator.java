package com.daniel.ddd.cart.application.service;

import com.daniel.mychickenbreastshop.domain.product.item.application.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartValidator {

    private final ProductService productService; // event publisher 하기

    public void cartValidate(CartRequestDto cartRequestDto) {
        productService.validatePayAmount(cartRequestDto.getItemNo(), cartRequestDto.getItemQuantity(),
                cartRequestDto.getTotalPrice());
    }
}
