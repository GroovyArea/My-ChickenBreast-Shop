package com.daniel.mychickenbreastshop.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.SimpleType;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * ObjectMapper를 이용한 객체, Json을 파싱하는 util class
 */
@Slf4j
public class JsonUtil {

    private static final String STRING_TO_OBJECT_EXCEPTION = "Failed object parsing";
    private static final String STRING_TO_MAP_EXCEPTION = "Failed map parsing";

    private static final ObjectMapper MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private JsonUtil() {
    }

    public static String objectToString(Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return "{}";
    }

    public static <T> T stringToObject(String string, Class<T> tClass) {
        try {
            return MAPPER.readValue(string, tClass);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(STRING_TO_OBJECT_EXCEPTION);
        }
    }

    public static <K, V> Map<K, V> stringToMap(String string, Class<K> keyType, Class<V> valueType) {
        try {
            return MAPPER.readValue(string, MapType.construct(HashMap.class, null, null,
                    null, SimpleType.constructUnsafe(keyType), SimpleType.constructUnsafe(valueType)));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(STRING_TO_MAP_EXCEPTION);
        }
    }
}
