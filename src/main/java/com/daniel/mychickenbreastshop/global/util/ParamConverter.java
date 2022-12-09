package com.daniel.mychickenbreastshop.global.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

@UtilityClass
public class ParamConverter {

    public static MultiValueMap<String, String> convert(ObjectMapper objectMapper, Object dto) {

        try {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            Map<String, String> map = objectMapper.convertValue(dto, new TypeReference<Map<String, String>>() {
            });
            params.setAll(map);

            return params;
        } catch (Exception e) {
            throw new IllegalStateException("Url Parameter 변환중 오류가 발생했습니다.");
        }
    }
}
