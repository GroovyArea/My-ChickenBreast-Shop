package com.daniel.mychickenbreastshop.domain.cart.util;

import com.daniel.mychickenbreastshop.global.util.JsonUtil;
import lombok.experimental.UtilityClass;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * 장바구니 쿠키 Utils <br>
 * 장바구니 쿠키 이용에 필요한 util 메서드를 구현
 *
 * <pre>
 *     <b>History</b>
 *     1.0, 2022.05.21 최초 작성
 *     1.1, 2022.05.29 총 가격, 상품 번호 배열 메서드 추가
 *     1.2, 2022.08.01 리팩토링 (제네릭 및 패키지 변경)
 * </pre>
 *
 * @author 김남영
 * @version 1.2
 */
@UtilityClass
public class CartCookieUtil {

    private static final String ENC_TYPE = "utf-8";

    /* 카트 쿠키 반환 메서드 */
    public static Optional<Cookie> getCartCookie(Cookie[] requestCookies, String cookieKey) {
        return Arrays.stream(requestCookies)
                .filter(cookie -> cookie.getName().equals(cookieKey))
                .findFirst();
    }

    /* 카트 쿠키 값 디코딩 후 map 객체 반환 메서드 */
    public static <V> Map<Integer, V> changeCookieToMap(Cookie responseCartCookie, Class<V> valueType) throws UnsupportedEncodingException {
        String cookieValue = responseCartCookie.getValue();
        return JsonUtil.stringToMap(URLDecoder.decode(cookieValue, ENC_TYPE), Integer.class, valueType);
    }

    /**
     *
     * @param responseCartCookie 응답용 장바구니 쿠키
     * @param clazz 변환 타입 clazz
     * @param <E> 변환 타입
     * @return 쿠키 값 리스트
     * @throws UnsupportedEncodingException 인코딩 예외
     */
    public static <E> List<E> getCookieValueList(Cookie responseCartCookie, Class<E> clazz) throws UnsupportedEncodingException {
        Map<Integer, E> map = changeCookieToMap(responseCartCookie, clazz);
        return new ArrayList<>(map.values());
    }
}
