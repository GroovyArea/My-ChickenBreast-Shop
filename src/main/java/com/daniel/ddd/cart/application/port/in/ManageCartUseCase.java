package com.daniel.ddd.cart.application.port.in;

import com.daniel.ddd.cart.model.dto.request.CartRequestDto;
import com.daniel.ddd.cart.model.dto.request.UpdatableCartDto;
import com.daniel.ddd.cart.model.dto.response.CartResponseDto;

import java.util.List;

public interface ManageCartUseCase {

    List<CartResponseDto> getCart(String cookieValue);

    UpdatableCartDto addCart(UpdatableCartDto updatableCartDto, CartRequestDto cartRequestDto);

    UpdatableCartDto modifyCart(UpdatableCartDto updatableCartDto, CartRequestDto cartRequestDto);

    UpdatableCartDto removeCart(UpdatableCartDto updatableCartDto, CartRequestDto cartRequestDto);
}
