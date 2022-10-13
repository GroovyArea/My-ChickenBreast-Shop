package com.daniel.mychickenbreastshop.usecase.orderpayment.extract;

import com.daniel.mychickenbreastshop.global.error.exception.InternalErrorException;
import com.daniel.mychickenbreastshop.global.util.CookieUtil;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.function.Function;

/**
 * 장바구니 쿠키 분해 및 프로퍼티 제공
 */
@Component
public class CartDisassembler {

    public <K, V> List<Long> getItemNumbers(String cookieValue, Class<K> kClass, Class<V> vClass, Function<V, Long> function) {
        try {
            return getCookieValues(cookieValue, kClass, vClass).stream()
                    .map(function)
                    .toList();
        } catch (UnsupportedEncodingException e) {
            throw new InternalErrorException(e);
        }
    }


    public <K, V> List<String> getItemNames(String cookieValue, Class<K> kClass, Class<V> vClass, Function<V, String> function) {
        try {
            return getCookieValues(cookieValue, kClass, vClass).stream()
                    .map(function)
                    .toList();
        } catch (UnsupportedEncodingException e) {
            throw new InternalErrorException(e);
        }
    }

    public <K, V> List<Integer> getItemQuantities(String cookieValue, Class<K> kClass, Class<V> vClass, Function<V, Integer> function) {
        try {
            return getCookieValues(cookieValue, kClass, vClass).stream()
                    .map(function)
                    .toList();
        } catch (UnsupportedEncodingException e) {
            throw new InternalErrorException(e);
        }
    }

    public <K, V> Long getTotalPrice(String cookieValue, Class<K> kClass, Class<V> vClass, Function<V, Long> function) {
        try {
            return getCookieValues(cookieValue, kClass, vClass).stream()
                    .map(function)
                    .mapToLong(Long::valueOf)
                    .sum();
        } catch (UnsupportedEncodingException e) {
            throw new InternalErrorException(e);
        }
    }

    private <K, V> List<V> getCookieValues(String cookieValue, Class<K> kClass, Class<V> vClass) throws UnsupportedEncodingException {
        return CookieUtil.getCookieValueList(cookieValue, kClass, vClass);
    }
}
