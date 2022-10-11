package com.daniel.mychickenbreastshop.domain.cart.application;

import com.daniel.mychickenbreastshop.domain.cart.application.manage.CartItemManager;
import com.daniel.mychickenbreastshop.domain.cart.application.validate.CartValidator;
import com.daniel.mychickenbreastshop.domain.cart.model.dto.request.CartRequestDto;
import com.daniel.mychickenbreastshop.domain.cart.model.dto.request.UpdatableCartDto;
import com.daniel.mychickenbreastshop.domain.cart.model.dto.response.CartResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemManager cartItemManager;
    private final CartValidator cartValidator;

    public List<CartResponseDto> getCart(String cookieValue) {
        return cartItemManager.getItems(cookieValue);
    }

    public UpdatableCartDto addCart(UpdatableCartDto updatableCartDto, CartRequestDto cartRequestDto) {
        cartValidator.cartValidate(cartRequestDto);
        return cartItemManager.store(updatableCartDto, cartRequestDto);

    }

    public UpdatableCartDto modifyCart(UpdatableCartDto updatableCartDto, CartRequestDto cartRequestDto) {
        cartValidator.cartValidate(cartRequestDto);
        return cartItemManager.update(updatableCartDto, cartRequestDto);
    }

    public UpdatableCartDto removeCart(UpdatableCartDto updatableCartDto, CartRequestDto cartRequestDto) {
        cartValidator.cartValidate(cartRequestDto);
        return cartItemManager.delete(updatableCartDto, cartRequestDto);
    }
}
