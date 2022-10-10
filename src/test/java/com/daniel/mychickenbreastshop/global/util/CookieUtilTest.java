package com.daniel.mychickenbreastshop.global.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class CookieUtilTest {

    Cookie[] cookies;
    Map<String, String> body;

    @BeforeEach
    void putCookies() {
        cookies = new Cookie[8];
        cookies[0] = new Cookie("a", "A");
        cookies[1] = new Cookie("b", "B");
        cookies[2] = new Cookie("c", "C");
        cookies[3] = new Cookie("d", "D");

        body = new HashMap<>();
        body.put("a","A");
        body.put("b","B");

        String encodedMap = URLEncoder.encode(JsonUtil.objectToString(body), StandardCharsets.UTF_8);

        Cookie dataCookie = new Cookie("data", encodedMap);

        cookies[4] = dataCookie;
    }

    @DisplayName("올바른 키 값으로 쿠키를 가져온다.")
    @Test
    void getCookieByKeyTest() {
        Cookie cookie = CookieUtil.getCookieByKey(cookies, "a").orElseThrow(() -> new RuntimeException("쿠키 없다~"));
        assertThat(cookie.getValue()).isEqualTo("A");
    }

    @DisplayName("인코딩 된 쿠키를 Map으로 변환한다.")
    @Test
    void cookieToMap() throws UnsupportedEncodingException {
        Cookie cookie = CookieUtil.getCookieByKey(cookies, "data").orElseThrow(() -> new RuntimeException("쿠키 없다~"));
        String cookieValue = cookie.getValue();

        Map<String, String> map = CookieUtil
                .cookieToMap(cookieValue, String.class, String.class);

        assertThat(map.get("a")).isEqualTo("A");
        assertThat(map.get("b")).isEqualTo("B");
    }

    @DisplayName("쿠키 값을 List로 반환한다.")
    @Test
    void getCookieValueList() throws UnsupportedEncodingException {
        Cookie cookie = CookieUtil.getCookieByKey(cookies, "data").orElseThrow(() -> new RuntimeException("쿠키 없다~"));
        String cookieValue = cookie.getValue();

        List<String> cookieValueList = CookieUtil.getCookieValueList(cookieValue, String.class, String.class);

        assertThat(cookieValueList.get(0)).isEqualTo("A");
        assertThat(cookieValueList.get(1)).isEqualTo("B");
    }

    @DisplayName("인코딩된 값을 담은 새로운 쿠키를 반환한다.")
    @Test
    void createCookie() {
        Cookie cookie = CookieUtil.getCookieByKey(cookies, "data").orElseThrow(() -> new RuntimeException("쿠키 없다~"));
        String cookieValue = cookie.getValue();

        Cookie newCookie = CookieUtil.createCookie("newCookie", cookieValue, "/api");

        assertThat(newCookie.getName()).isEqualTo("newCookie");
        assertThat(newCookie.getPath()).isEqualTo("/api");
    }
}