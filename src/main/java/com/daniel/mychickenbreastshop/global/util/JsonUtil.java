package com.daniel.mychickenbreastshop.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.SimpleType;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ObjectMapper를 이용한 객체, Json을 파싱하는 util class
 */
@Slf4j
@UtilityClass
public class JsonUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//            .registerModule(new JavaTimeModule())
//            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public static String objectToString(Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return "{}";
    }

    public static <T> T stringToObject(String string, Class<T> tClass) {
        try {
            return MAPPER.readValue(string, tClass);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static <K, V> Map<K, V> stringToMap(String string, Class<K> keyType, Class<V> valueType) {
        try {
            return MAPPER.readValue(string, MapType.construct(ConcurrentHashMap.class, null, null,
                    null, SimpleType.constructUnsafe(keyType), SimpleType.constructUnsafe(valueType)));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
