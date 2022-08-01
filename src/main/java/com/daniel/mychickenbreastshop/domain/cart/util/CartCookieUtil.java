package com.daniel.mychickenbreastshop.domain.cart.util;

import com.daniel.mychickenbreastshop.domain.cart.dto.request.CartRequestDTO;
import com.daniel.mychickenbreastshop.global.util.JsonUtil;
import lombok.experimental.UtilityClass;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * 장바구니 쿠키 Utils <br>
 * 장바구니 쿠키 이용에 필요한 util 메서드를 구현
 *
 * <pre>
 *     <b>History</b>
 *     김남영, 1.0, 2022.05.21 최초 작성
 *     김남영, 1.1, 2022.05.29 총 가격, 상품 번호 배열 메서드 추가
 *     김남영, 1.2, 2022.08.01 리팩토링 (제네릭 및 패키지 변경)
 * </pre>
 *
 * @author 김남영
 * @version 1.2
 */
@UtilityClass
public class CartCookieUtil {

    private static final String COOKIE_KEY = "Chicken";
    private static final String ENC_TYPE = "utf-8";

    /* 카트 쿠키 반환 메서드 */
    public static Optional<Cookie> getCartCookie(Cookie[] requestCookies) {
        return Arrays.stream(requestCookies)
                .filter(cookie -> cookie.getName().equals(COOKIE_KEY))
                .findFirst();
    }

    /* 카트 쿠키 값 디코딩 후 map 객체 반환 메서드 */
    public static <V> Map<Integer, V> getCartItemDTOMap(Cookie responseCartCookie, Class<V> valueType) throws UnsupportedEncodingException {
        String cookieValue = responseCartCookie.getValue();
        return JsonUtil.stringToMap(URLDecoder.decode(cookieValue, ENC_TYPE), Integer.class, valueType);
    }

    /**
     * 카트 안의 상품 번호 배열 반환 메서드
     *
     * @param responseCartCookie 응답 카트 쿠키
     * @return 상품 번호 배열
     * @throws UnsupportedEncodingException 인코딩 예외
     */
    public static Integer[] getItemNoArr(Cookie responseCartCookie) throws UnsupportedEncodingException {
        return Arrays.stream(getCartArr(responseCartCookie))
                .map(CartRequestDTO::getProductNo)
                .toArray(Integer[]::new);
    }

    /**
     * 카트 안의 상품 이름 배열 반환 메서드
     *
     * @param responseCartCookie 응답 카트 쿠키
     * @return 상품 이름 배열
     * @throws UnsupportedEncodingException 인코딩 예외
     */
    public static String[] getItemNameArr(Cookie responseCartCookie) throws UnsupportedEncodingException {
        return Arrays.stream(getCartArr(responseCartCookie))
                .map(CartRequestDTO::getProductName)
                .toArray(String[]::new);
    }

    /**
     * 카트 안의 상품 재고량 배열 반환 메서드
     *
     * @param responseCartCookie 응답 카트 쿠키
     * @return 상품 재고량 배열
     * @throws UnsupportedEncodingException 인코딩 예외
     */
    public static Integer[] getStockArr(Cookie responseCartCookie) throws UnsupportedEncodingException {
        return Arrays.stream(getCartArr(responseCartCookie))
                .map(CartRequestDTO::getProductStock)
                .toArray(Integer[]::new);
    }

    /**
     * 카트 안의 상품 총 가격 반환 메서드
     *
     * @param responseCartCookie 응답 카트 쿠키
     * @return 상품 총 가격
     * @throws UnsupportedEncodingException 인코딩 예외
     */
    public static int getTotalAmount(Cookie responseCartCookie) throws UnsupportedEncodingException {
        return Arrays.stream(getCartArr(responseCartCookie))
                .map(CartRequestDTO::getProductPrice)
                .mapToInt(Integer::intValue)
                .sum();
    }

    private static CartRequestDTO[] getCartArr(Cookie responseCartCookie) throws UnsupportedEncodingException {
        Map<Integer, CartRequestDTO> cartItemDTOMap = getCartItemDTOMap(responseCartCookie, CartRequestDTO.class);
        Collection<CartRequestDTO> values = cartItemDTOMap.values();
        return values.toArray(new CartRequestDTO[0]);
    }
}
