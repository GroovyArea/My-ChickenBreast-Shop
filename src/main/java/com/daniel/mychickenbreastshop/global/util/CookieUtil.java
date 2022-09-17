package com.daniel.mychickenbreastshop.global.util;

import lombok.experimental.UtilityClass;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 쿠키 Utils <br>
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
public class CookieUtil {

    private static final int COOKIE_AGE = 60 * 60 * 24 * 7;
    private static final int KILL_COOKIE = 0;

    /**
     * 쿠키 키를 이용한 쿠키 반환
     *
     * @param requestCookies request에 실린 쿠키 배열
     * @param cookieKey      쿠키 키
     * @return 쿠키
     */
    public static Optional<Cookie> getCookieByKey(Cookie[] requestCookies, String cookieKey) {
        return Arrays.stream(requestCookies)
                .filter(cookie -> cookie.getName().equals(cookieKey))
                .findFirst();
    }

    /**
     * 쿠키 값을 이용해 Map 반환
     *
     * @param cookieValue 문자열 쿠키 값
     * @param valueType   값 타입
     * @param <V>         값 타입
     * @return 쿠키 값 Map
     * @throws UnsupportedEncodingException 인코딩 예외
     */
    private static <K, V> Map<K, V> cookieToMap(String cookieValue, Class<K> keyType, Class<V> valueType) throws UnsupportedEncodingException {
        return JsonUtil.stringToMap(URLDecoder.decode(cookieValue, StandardCharsets.UTF_8), keyType, valueType);
    }

    /**
     * 쿠키 값 리스트 반환
     *
     * @param cookieValue 문자열 쿠키 값
     * @param valueType   값 타입
     * @param <E>         값 타입
     * @return 쿠키 값 리스트
     * @throws UnsupportedEncodingException 인코딩 예외
     */
    public static <K, E> List<E> getCookieValueList(String cookieValue, Class<K> keyType, Class<E> valueType) throws UnsupportedEncodingException {
        Map<K, E> map = cookieToMap(cookieValue, keyType, valueType);
        return new ArrayList<>(map.values());
    }

    public static boolean isCookieEmpty(Cookie cookie) {
        return cookie == null;
    }

    public static Cookie createCookie(String key, String encodedObjectValue, String path) {
        Cookie newCookie = new Cookie(key, encodedObjectValue);
        newCookie.setPath(path);
        newCookie.setMaxAge(COOKIE_AGE);
        return newCookie;
    }

    public static Cookie resetCookie(String key, Cookie existingCookie, String encodedObjectValue, String path) {
        existingCookie.setMaxAge(KILL_COOKIE);
        return createCookie(key, encodedObjectValue, path);
    }
}
