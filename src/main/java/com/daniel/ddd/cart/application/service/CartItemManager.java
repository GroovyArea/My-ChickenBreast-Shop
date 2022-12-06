package com.daniel.ddd.cart.application.service;


import com.daniel.ddd.cart.model.dto.request.CartRequestDto;
import com.daniel.ddd.cart.model.dto.request.UpdatableCartDto;
import com.daniel.ddd.cart.model.dto.response.CartResponseDto;
import com.daniel.ddd.cart.model.enums.CartProperty;
import com.daniel.ddd.cart.model.enums.ErrorMessages;
import com.daniel.ddd.global.error.exception.BadRequestException;
import com.daniel.ddd.global.error.exception.InternalErrorException;
import com.daniel.ddd.global.util.CookieUtil;
import com.daniel.ddd.global.util.JsonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CartItemManager {

    @Value("{spring.cart.path}")
    private String path;

    public List<CartResponseDto> getItems(String cookieValue) {
        try {
            return CookieUtil.getCookieValueList(cookieValue, Long.class, CartResponseDto.class);
        } catch (UnsupportedEncodingException e) {
            throw new InternalErrorException(e);
        }
    }

    public UpdatableCartDto store(UpdatableCartDto updatableCartDto, CartRequestDto cartRequestDto) {
        if (CookieUtil.isCookieEmpty(updatableCartDto.getCookie())) {
            Map<Long, CartRequestDto> newMap = new HashMap<>();

            addItemToMap(newMap, cartRequestDto);

            String encodedObjectValue = URLEncoder.encode(JsonUtil.objectToString(newMap), StandardCharsets.UTF_8);

            return UpdatableCartDto.builder()
                    .cookie(CookieUtil.createCookie(CartProperty.COOKIE_KEY.getKey(), encodedObjectValue, path))
                    .build();
        }

        Map<Long, CartRequestDto> existingMap = getCartRequestDtoMap(updatableCartDto);

        addItemToMap(existingMap, cartRequestDto);

        return getUpdatableCartDto(updatableCartDto, existingMap);
    }

    public UpdatableCartDto update(UpdatableCartDto updatableCartDto, CartRequestDto cartRequestDto) {
        if (CookieUtil.isCookieEmpty(updatableCartDto.getCookie())) {
            throw new BadRequestException(ErrorMessages.MODIFIABLE_COOKIE_NOT_EXISTS.getMessage());
        }

        Map<Long, CartRequestDto> existingMap = getCartRequestDtoMap(updatableCartDto);

        addItemToMap(existingMap, cartRequestDto);

        return getUpdatableCartDto(updatableCartDto, existingMap);
    }


    public UpdatableCartDto delete(UpdatableCartDto updatableCartDto, CartRequestDto cartRequestDto) {
        if (CookieUtil.isCookieEmpty(updatableCartDto.getCookie())) {
            throw new BadRequestException(ErrorMessages.REMOVABLE_COOKIE_NOT_EXISTS.getMessage());
        }

        Map<Long, CartRequestDto> existingMap = getCartRequestDtoMap(updatableCartDto);

        existingMap.remove(cartRequestDto.getItemNo());

        return getUpdatableCartDto(updatableCartDto, existingMap);
    }

    private void addItemToMap(Map<Long, CartRequestDto> existingMap, CartRequestDto cartRequestDto) {
        existingMap.put(cartRequestDto.getItemNo(), cartRequestDto);
    }

    private UpdatableCartDto getUpdatableCartDto(UpdatableCartDto updatableCartDto, Map<Long, CartRequestDto> existingMap) {
        String encodedObjectValue = URLEncoder.encode(JsonUtil.objectToString(existingMap), StandardCharsets.UTF_8);
        updatableCartDto.getCookie().setValue(encodedObjectValue);
        return updatableCartDto;
    }


    private Map<Long, CartRequestDto> getCartRequestDtoMap(UpdatableCartDto updatableCartDto) {
        try {
            return CookieUtil.cookieToMap(updatableCartDto.getCookie().getValue(),
                    Long.class, CartRequestDto.class);
        } catch (UnsupportedEncodingException e) {
            throw new InternalErrorException(e);
        }
    }
}
