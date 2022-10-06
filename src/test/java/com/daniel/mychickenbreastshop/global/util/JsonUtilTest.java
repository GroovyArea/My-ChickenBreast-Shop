package com.daniel.mychickenbreastshop.global.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class JsonUtilTest {

    Map<String, String> body;

    @BeforeEach
    void beforeTest() {
        body = new HashMap<>();
        body.put("1", "1");
        body.put("2", "2");
    }

    @DisplayName("객체를 String으로 반환한다.")
    @Test
    void objectToStringTest() {
        assertThat(JsonUtil.objectToString(body).getClass()).isEqualTo(String.class);
    }

    @DisplayName("String을 Map으로 변환한다.")
    @Test
    void stringToMapTest() {
        String parsedMap = JsonUtil.objectToString(body);
        Map<String, String> parsedString = JsonUtil.stringToMap(parsedMap, String.class, String.class);

        assertThat(parsedString.get("1")).isEqualTo("1");
    }

    @Test
    @DisplayName("잘못된 값을 Map으로 변환 시에 예외가 발생한다.")
    void wrongValueTest() {
        String wrongValue = "asdfasdfasd123123";
        assertThrows(IllegalArgumentException.class, () -> JsonUtil.stringToMap(wrongValue, String.class, String.class));
    }

}