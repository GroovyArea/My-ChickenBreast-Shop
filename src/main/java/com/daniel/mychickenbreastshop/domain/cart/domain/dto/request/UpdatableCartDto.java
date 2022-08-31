package com.daniel.mychickenbreastshop.domain.cart.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.servlet.http.Cookie;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatableCartDto {

    private String cookieValue;
    private Map<Integer, CartRequestDto> cartRequestDTOMap;
    private Cookie cookie;
}
