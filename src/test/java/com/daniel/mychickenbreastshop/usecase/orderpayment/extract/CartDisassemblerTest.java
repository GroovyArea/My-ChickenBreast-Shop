package com.daniel.mychickenbreastshop.usecase.orderpayment.extract;

import com.daniel.mychickenbreastshop.global.util.CookieUtil;
import com.daniel.mychickenbreastshop.global.util.JsonUtil;
import com.daniel.mychickenbreastshop.usecase.orderpayment.extract.model.CartItem;
import com.daniel.mychickenbreastshop.usecase.orderpayment.extract.model.CartValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.Cookie;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CartDisassemblerTest {

    private String cookieValue;

    private CartDisassembler cartDisassembler;

    @BeforeEach
    void setUp() {
        cartDisassembler = new CartDisassembler();
        List<CartItem> cartItems = new ArrayList<>();

        Map itemMap = new HashMap<>();

        for (int i = 1; i <= 5; i++) {
            CartItem cartItem = CartItem.builder()
                    .itemNo((long) i)
                    .itemName("itemName" + i)
                    .itemQuantity(i)
                    .totalPrice((long) (1000 * i))
                    .build();

            cartItems.add(cartItem);

            itemMap.put(cartItem.getItemNo(), cartItem);
        }

        String encodedValue = URLEncoder.encode(JsonUtil.objectToString(itemMap), StandardCharsets.UTF_8);

        Cookie cookie = CookieUtil.createCookie("chicken", encodedValue, "/api");

        cookieValue = cookie.getValue();
    }

    @Test
    void getCartValue() {
        CartValue cartValue = cartDisassembler.getCartValue(cookieValue);

        assertThat(cartValue.getItemNumbers()).hasSize(5);
        cartValue.getItemNames().forEach(itemName -> assertThat(itemName).contains("itemName"));
        assertThat(cartValue.getItemQuantities()).hasSize(5);
        assertThat(cartValue.getTotalPrice()).isEqualTo(15000);
    }
}