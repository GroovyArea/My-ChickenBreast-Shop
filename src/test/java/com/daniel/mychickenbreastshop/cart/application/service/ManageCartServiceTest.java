package com.daniel.mychickenbreastshop.cart.application.service;

import com.daniel.mychickenbreastshop.cart.model.dto.request.CartRequestDto;
import com.daniel.mychickenbreastshop.cart.model.dto.request.UpdatableCartDto;
import com.daniel.mychickenbreastshop.cart.model.dto.response.CartResponseDto;
import com.daniel.mychickenbreastshop.cart.model.enums.CartProperty;
import com.daniel.mychickenbreastshop.global.util.CookieUtil;
import com.daniel.mychickenbreastshop.global.util.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ManageCartServiceTest {

    @Mock
    private  CartItemManager cartItemManager;

    @Mock
    private  CartValidator cartValidator;

    @InjectMocks
    private ManageCartService manageCartService;

    private String cookieValue;
    private UpdatableCartDto updatableCartDto;
    private Map<Long, CartRequestDto> cartMap;

    @BeforeEach
    void setUp() {
        CartRequestDto cartRequestDto = CartRequestDto.builder()
                .itemNo(1L)
                .itemName("itemName1")
                .itemQuantity(100)
                .totalPrice(10000L)
                .build();

        CartRequestDto cartRequestDto2 = CartRequestDto.builder()
                .itemNo(2L)
                .itemName("itemName2")
                .itemQuantity(100)
                .totalPrice(10000L)
                .build();

        cartMap = new ConcurrentHashMap<>();
        cartMap.put(cartRequestDto.getItemNo(), cartRequestDto);
        cartMap.put(cartRequestDto2.getItemNo(), cartRequestDto2);

        String encodedValue = URLEncoder.encode(JsonUtil.objectToString(cartMap), StandardCharsets.UTF_8);
        cookieValue = encodedValue;

        updatableCartDto = UpdatableCartDto.builder()
                .cookie(CookieUtil.createCookie(CartProperty.COOKIE_KEY.getKey(), encodedValue, "/api/**"))
                .build();
    }

    @DisplayName("??????????????? ?????? ?????? ????????? ????????????.")
    @Test
    void getCart() throws UnsupportedEncodingException {
        // given
        List<CartResponseDto> dtos = CookieUtil.getCookieValueList(cookieValue, Long.class, CartResponseDto.class);

        // when
        when(cartItemManager.getItems(cookieValue)).thenReturn(dtos);
        List<CartResponseDto> cart = manageCartService.getCart(cookieValue);

        assertThat(cart).hasSize(2);
    }

    @DisplayName("??????????????? ????????? ????????????.")
    @Test
    void addCart() throws UnsupportedEncodingException {
        // given
        CartRequestDto cartRequestDto3 = CartRequestDto.builder()
                .itemNo(3L)
                .itemName("itemName3")
                .itemQuantity(100)
                .totalPrice(10000L)
                .build();

        cartMap.put(cartRequestDto3.getItemNo(), cartRequestDto3);

        String encodedValue = URLEncoder.encode(JsonUtil.objectToString(cartMap), StandardCharsets.UTF_8);
        cookieValue = encodedValue;

        updatableCartDto = UpdatableCartDto.builder()
                .cookie(CookieUtil.createCookie(CartProperty.COOKIE_KEY.getKey(), encodedValue, "/api/**"))
                .build();

        // when
        when(cartItemManager.store(updatableCartDto, cartRequestDto3)).thenReturn(updatableCartDto);
        UpdatableCartDto cartDto = manageCartService.addCart(updatableCartDto, cartRequestDto3);
        List<CartResponseDto> responseDtos = CookieUtil.getCookieValueList(cartDto.getCookie().getValue(), Long.class, CartResponseDto.class);

        assertThat(responseDtos).hasSize(3);
        assertThat(responseDtos.get(2).getItemNo()).isEqualTo(3L);
        assertThat(responseDtos.get(2).getItemName()).isEqualTo("itemName3");
    }

    @DisplayName("??????????????? ?????? ????????? ????????????.")
    @Test
    void modifyCart() throws UnsupportedEncodingException {
        // given
        CartRequestDto cartRequestDto2 = CartRequestDto.builder()
                .itemNo(2L)
                .itemName("itemName2")
                .itemQuantity(50)
                .totalPrice(5000L)
                .build();

        cartMap.put(cartRequestDto2.getItemNo(), cartRequestDto2);

        String encodedValue = URLEncoder.encode(JsonUtil.objectToString(cartMap), StandardCharsets.UTF_8);
        cookieValue = encodedValue;

        updatableCartDto = UpdatableCartDto.builder()
                .cookie(CookieUtil.createCookie(CartProperty.COOKIE_KEY.getKey(), encodedValue, "/api/**"))
                .build();

        // when
        when(cartItemManager.update(updatableCartDto, cartRequestDto2)).thenReturn(updatableCartDto);
        UpdatableCartDto cartDto = manageCartService.modifyCart(updatableCartDto, cartRequestDto2);
        List<CartResponseDto> responseDtos = CookieUtil.getCookieValueList(cartDto.getCookie().getValue(), Long.class, CartResponseDto.class);

        assertThat(responseDtos.get(1).getItemQuantity()).isEqualTo(50);
        assertThat(responseDtos.get(1).getTotalPrice()).isEqualTo(5000L);
    }

    @DisplayName("??????????????? ?????? ????????? ????????????.")
    @Test
    void removeCart() throws UnsupportedEncodingException {
        // given
        CartRequestDto cartRequestDto2 = CartRequestDto.builder()
                .itemNo(2L)
                .itemName("itemName2")
                .itemQuantity(100)
                .totalPrice(10000L)
                .build();

        cartMap.remove(cartRequestDto2.getItemNo());

        String encodedValue = URLEncoder.encode(JsonUtil.objectToString(cartMap), StandardCharsets.UTF_8);
        cookieValue = encodedValue;

        updatableCartDto = UpdatableCartDto.builder()
                .cookie(CookieUtil.createCookie(CartProperty.COOKIE_KEY.getKey(), encodedValue, "/api/**"))
                .build();

        // when
        when(cartItemManager.delete(updatableCartDto, cartRequestDto2)).thenReturn(updatableCartDto);
        UpdatableCartDto cartDto = manageCartService.removeCart(updatableCartDto, cartRequestDto2);
        List<CartResponseDto> responseDtos = CookieUtil.getCookieValueList(cartDto.getCookie().getValue(), Long.class, CartResponseDto.class);

        assertThat(responseDtos).hasSize(1);
    }
}