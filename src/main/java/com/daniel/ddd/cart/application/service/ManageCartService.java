package com.daniel.ddd.cart.application.service;

import com.daniel.ddd.cart.application.port.in.ManageCartUseCase;
import com.daniel.ddd.cart.model.dto.request.CartRequestDto;
import com.daniel.ddd.cart.model.dto.request.UpdatableCartDto;
import com.daniel.ddd.cart.model.dto.response.CartResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManageCartService implements ManageCartUseCase {

    private final CartItemManager cartItemManager;
    private final CartValidator cartValidator;

    @Override
    public List<CartResponseDto> getCart(String cookieValue) {
        return cartItemManager.getItems(cookieValue);
    }

    @Override
    public UpdatableCartDto addCart(UpdatableCartDto updatableCartDto, CartRequestDto cartRequestDto) {
        validateCart(cartRequestDto);
        return cartItemManager.store(updatableCartDto, cartRequestDto);
    }

    @Override
    public UpdatableCartDto modifyCart(UpdatableCartDto updatableCartDto, CartRequestDto cartRequestDto) {
        validateCart(cartRequestDto);
        return cartItemManager.update(updatableCartDto, cartRequestDto);
    }

    @Override
    public UpdatableCartDto removeCart(UpdatableCartDto updatableCartDto, CartRequestDto cartRequestDto) {
        validateCart(cartRequestDto);
        return cartItemManager.delete(updatableCartDto, cartRequestDto);
    }

    private void validateCart(CartRequestDto cartRequestDto) {
        cartValidator.cartValidate(cartRequestDto);
    }
}
