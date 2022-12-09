package com.daniel.mychickenbreastshop.domain.cart.application.manage;

import com.daniel.mychickenbreastshop.cart.application.service.CartItemManager;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CartItemManagerTest {

    private CartItemManager cartItemManager;

    private String cookieValue;
    private UpdatableCartDto updatableCartDto;

    @BeforeEach
    void setUp() {
        cartItemManager = new CartItemManager();
        ReflectionTestUtils.setField(cartItemManager, "path", "/api/**");

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

        Map<Long, CartRequestDto> map = new HashMap();
        map.put(cartRequestDto.getItemNo(), cartRequestDto);
        map.put(cartRequestDto2.getItemNo(), cartRequestDto2);

        String encodedValue = URLEncoder.encode(JsonUtil.objectToString(map), StandardCharsets.UTF_8);
        cookieValue = encodedValue;

        updatableCartDto = UpdatableCartDto.builder()
                .cookie(CookieUtil.createCookie(CartProperty.COOKIE_KEY.getKey(), encodedValue, "/api/**"))
                .build();
    }

    @DisplayName("쿠키의 값을 읽어 장바구니의 상품 목록을 반환한다.")
    @Test
    void getItems() {

        List<CartResponseDto> items = cartItemManager.getItems(cookieValue);

        assertThat(items).hasSize(2);
        items.forEach(dto -> assertThat(dto.getItemName()).contains("itemName"));
        items.forEach(dto -> assertThat(dto.getItemQuantity()).isEqualTo(100));
    }

    @DisplayName("장바구니에 상품을 저장한다.")
    @Test
    void store() throws UnsupportedEncodingException {
        // given
        CartRequestDto cartRequestDto3 = CartRequestDto.builder()
                .itemNo(3L)
                .itemName("itemName3")
                .itemQuantity(100)
                .totalPrice(10000L)
                .build();

        // when
        UpdatableCartDto cartDto = cartItemManager.store(updatableCartDto, cartRequestDto3);
        List<CartResponseDto> dtos = CookieUtil.getCookieValueList(cartDto.getCookie().getValue(), Long.class, CartResponseDto.class);

        assertThat(dtos).hasSize(3);
        assertThat(dtos.get(2).getItemNo()).isEqualTo(3L);
        assertThat(dtos.get(2).getItemName()).isEqualTo("itemName3");
    }

    @DisplayName("장바구니의 상품을 수정한다.")
    @Test
    void update() throws UnsupportedEncodingException {
        // given
        CartRequestDto cartRequestDto2 = CartRequestDto.builder()
                .itemNo(2L)
                .itemName("itemName3")
                .itemQuantity(50)
                .totalPrice(5000L)
                .build();

        // when
        UpdatableCartDto cartDto = cartItemManager.update(updatableCartDto, cartRequestDto2);
        List<CartResponseDto> dtos = CookieUtil.getCookieValueList(cartDto.getCookie().getValue(), Long.class, CartResponseDto.class);

        assertThat(dtos.get(1).getItemQuantity()).isEqualTo(50);
        assertThat(dtos.get(1).getTotalPrice()).isEqualTo(5000L);
    }

    @DisplayName("장바구니의 상품을 삭제한다.")
    @Test
    void delete() throws UnsupportedEncodingException {
        // given
        CartRequestDto cartRequestDto2 = CartRequestDto.builder()
                .itemNo(2L)
                .itemName("itemName3")
                .itemQuantity(100)
                .totalPrice(10000L)
                .build();

        // when
        UpdatableCartDto cartDto = cartItemManager.delete(updatableCartDto, cartRequestDto2);
        List<CartResponseDto> dtos = CookieUtil.getCookieValueList(cartDto.getCookie().getValue(), Long.class, CartResponseDto.class);

        assertThat(dtos).hasSize(1);
    }

}