package com.daniel.mychickenbreastshop.usecase.orderpayment.extract;

import com.daniel.mychickenbreastshop.global.error.exception.InternalErrorException;
import com.daniel.mychickenbreastshop.global.util.CookieUtil;
import com.daniel.mychickenbreastshop.usecase.orderpayment.extract.model.CartItem;
import com.daniel.mychickenbreastshop.usecase.orderpayment.extract.model.CartValue;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 장바구니 쿠키 분해 및 프로퍼티 제공
 */
@Component
public class CartDisassembler {

    private List<Long> getItemNumbers(String cookieValue) {
        try {
            return getCookieValues(cookieValue).stream()
                    .map(CartItem::getItemNo)
                    .toList();
        } catch (UnsupportedEncodingException e) {
            throw new InternalErrorException(e);
        }
    }

    private List<String> getItemNames(String cookieValue) {
        try {
            return getCookieValues(cookieValue).stream()
                    .map(CartItem::getItemName)
                    .toList();
        } catch (UnsupportedEncodingException e) {
            throw new InternalErrorException(e);
        }
    }

    private List<Integer> getItemQuantities(String cookieValue) {
        try {
            return getCookieValues(cookieValue).stream()
                    .map(CartItem::getItemQuantity)
                    .toList();
        } catch (UnsupportedEncodingException e) {
            throw new InternalErrorException(e);
        }
    }

    private Long getTotalPrice(String cookieValue) {
        try {
            return getCookieValues(cookieValue).stream()
                    .map(CartItem::getTotalPrice)
                    .mapToLong(Long::valueOf)
                    .sum();
        } catch (UnsupportedEncodingException e) {
            throw new InternalErrorException(e);
        }
    }

    private List<CartItem> getCookieValues(String cookieValue) throws UnsupportedEncodingException {
        return CookieUtil.getCookieValueList(cookieValue, Long.class, CartItem.class);
    }

    public CartValue getCartValue(String cookieValue) {
            return CartValue.builder()
                    .itemNumbers(getItemNumbers(cookieValue))
                    .itemNames(getItemNames(cookieValue))
                    .itemQuantities(getItemQuantities(cookieValue))
                    .totalPrice(getTotalPrice(cookieValue))
                    .build();
    }
}
