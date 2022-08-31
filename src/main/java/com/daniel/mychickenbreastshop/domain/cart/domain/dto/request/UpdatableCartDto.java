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
    private Map<Long, CartRequestDto> cartRequestDTOMap; // Key : 상품 번호, Value : 변경 상품 dto
    private Cookie cookie;
}
