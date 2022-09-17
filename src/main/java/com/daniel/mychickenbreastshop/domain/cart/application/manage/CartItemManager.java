package com.daniel.mychickenbreastshop.domain.cart.application.manage;

import com.daniel.mychickenbreastshop.domain.cart.domain.dto.request.CartRequestDto;
import com.daniel.mychickenbreastshop.domain.cart.domain.dto.request.UpdatableCartDto;
import com.daniel.mychickenbreastshop.domain.cart.domain.model.CartProperty;
import com.daniel.mychickenbreastshop.domain.cart.domain.model.CartResponse;
import com.daniel.mychickenbreastshop.global.util.CookieUtil;
import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import com.daniel.mychickenbreastshop.global.util.JsonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class CartItemManager {

    @Value("{spring.cart.path}")
    private String path;

    public UpdatableCartDto store(UpdatableCartDto updatableCartDto, CartRequestDto cartRequestDto) {
        if (CookieUtil.isCookieEmpty(updatableCartDto.getCookie())) {
            Map<Long, CartRequestDto> newMap = new HashMap<>();

            newMap.put(cartRequestDto.getItemNo(), cartRequestDto);

            String encodedObjectValue = URLEncoder.encode(JsonUtil.objectToString(newMap), StandardCharsets.UTF_8);

            return UpdatableCartDto.builder()
                    .cookie(CookieUtil.createCookie(CartProperty.COOKIE_KEY.getKey(), encodedObjectValue, path))
                    .build();
        }

        Map<Long, CartRequestDto> existingMap = getCartRequestDtoMap(updatableCartDto);

        existingMap.put(cartRequestDto.getItemNo(), cartRequestDto);

        return getUpdatableCartDto(updatableCartDto, existingMap);
    }

    public UpdatableCartDto update(UpdatableCartDto updatableCartDto, CartRequestDto cartRequestDto) {
        if (CookieUtil.isCookieEmpty(updatableCartDto.getCookie())) {
            throw new BadRequestException(CartResponse.MODIFIABLE_COOKIE_NOT_EXISTS.getMessage());
        }

        Map<Long, CartRequestDto> existingMap = getCartRequestDtoMap(updatableCartDto);

        existingMap.put(cartRequestDto.getItemNo(), cartRequestDto);

        return getUpdatableCartDto(updatableCartDto, existingMap);
    }

    public UpdatableCartDto delete(UpdatableCartDto updatableCartDto, CartRequestDto cartRequestDto) {
        if (CookieUtil.isCookieEmpty(updatableCartDto.getCookie())) {
            throw new BadRequestException(CartResponse.REMOVABLE_COOKIE_NOT_EXISTS.getMessage());
        }

        Map<Long, CartRequestDto> existingMap = getCartRequestDtoMap(updatableCartDto);

        existingMap.remove(cartRequestDto.getItemNo());

        return getUpdatableCartDto(updatableCartDto, existingMap);
    }

    private UpdatableCartDto getUpdatableCartDto(UpdatableCartDto updatableCartDto, Map<Long, CartRequestDto> existingMap) {
        String encodedObjectValue = URLEncoder.encode(JsonUtil.objectToString(existingMap), StandardCharsets.UTF_8);

        return UpdatableCartDto.builder()
                .cookie(CookieUtil.resetCookie(CartProperty.COOKIE_KEY.getKey(), updatableCartDto.getCookie(), encodedObjectValue, path))
                .build();
    }


    private Map<Long, CartRequestDto> getCartRequestDtoMap(UpdatableCartDto updatableCartDto) {
        return JsonUtil.stringToMap(URLDecoder.decode(updatableCartDto.getCookie().getValue(), StandardCharsets.UTF_8),
                Long.class, CartRequestDto.class);
    }


}
