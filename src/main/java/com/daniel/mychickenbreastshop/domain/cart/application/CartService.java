package com.daniel.mychickenbreastshop.domain.cart.application;

import com.daniel.mychickenbreastshop.domain.cart.domain.dto.request.UpdatableCartDto;
import com.daniel.mychickenbreastshop.domain.cart.domain.dto.request.CartRequestDto;
import com.daniel.mychickenbreastshop.domain.cart.domain.dto.response.CartResponseDto;
import com.daniel.mychickenbreastshop.global.util.CookieUtil;
import com.daniel.mychickenbreastshop.domain.cart.application.manage.CartItemManager;
import com.daniel.mychickenbreastshop.global.error.exception.InternalErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemManager cartItemManager;
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
