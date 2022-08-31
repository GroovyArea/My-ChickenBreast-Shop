package com.daniel.mychickenbreastshop.domain.cart.application;

import com.daniel.mychickenbreastshop.domain.cart.domain.dto.request.UpdatableCartDto;
import com.daniel.mychickenbreastshop.domain.cart.domain.dto.request.CartRequestDto;
import com.daniel.mychickenbreastshop.domain.cart.domain.dto.response.CartResponseDto;
import com.daniel.mychickenbreastshop.domain.cart.util.CookieUtil;
import com.daniel.mychickenbreastshop.domain.cart.application.store.CartItemStore;
import com.daniel.mychickenbreastshop.global.error.exception.InternalErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemStore cartItemStore;
    private final CartValidator cartValidator;

    public List<CartResponseDto> getCart(String cookieValue) {
        try {
            return CookieUtil.getCookieValueList(cookieValue, Long.class, CartResponseDto.class);
        } catch (UnsupportedEncodingException e) {
            throw new InternalErrorException(e);
        }
    }


    public UpdatableCartDto addCart(UpdatableCartDto updatableCartDto, CartRequestDto cartRequestDto) {
        cartValidator.cartValidate(cartRequestDto);
        return cartItemStore.store(updatableCartDto, cartRequestDto);

    }

    public UpdatableCartDto modifyCart(UpdatableCartDto updatableCartDto, CartRequestDto cartRequestDto) {
        cartValidator.cartValidate(cartRequestDto);
        return cartItemStore.update(updatableCartDto, cartRequestDto);
    }

    public UpdatableCartDto removeCart(UpdatableCartDto updatableCartDto, CartRequestDto cartRequestDto) {
        cartValidator.cartValidate(cartRequestDto);
        return cartItemStore.delete(updatableCartDto, cartRequestDto);
    }
}
