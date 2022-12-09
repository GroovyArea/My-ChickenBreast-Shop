package com.daniel.mychickenbreastshop.cart.adaptor.in.web.rest;


import com.daniel.mychickenbreastshop.cart.application.port.in.ManageCartUseCase;
import com.daniel.mychickenbreastshop.cart.model.dto.request.CartRequestDto;
import com.daniel.mychickenbreastshop.cart.model.dto.request.UpdatableCartDto;
import com.daniel.mychickenbreastshop.cart.model.dto.response.CartResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 장바구니 컨트롤러
 *
 * <pre>
 *     <b>history</b>
 *     1.0 2022.08.30 최초 작성
 *     1.1 2022.12.06 패키지 구조 변경
 * </pre>
 *
 * @author Daniel Kim
 * @version 1.1
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")
public class CartApiController {

    private final ManageCartUseCase cartUseCase;

    /**
     * 장바구니 상품 조회
     *
     * @param cookie 쿠키
     */
    @GetMapping
    public ResponseEntity<List<CartResponseDto>> getCartList(@CookieValue(value = "chicken") Cookie cookie) {
        List<CartResponseDto> cartResponseDtos = cartUseCase.getCart(cookie.getValue());
        return ResponseEntity.ok(cartResponseDtos);
    }

    /**
     * 장바구니에 상품 추가
     *
     * @param cookie            쿠키
     * @param addCartRequestDTO 추가할 상품
     * @param response          response
     */
    @PostMapping
    public ResponseEntity<Void> addCart(@CookieValue(value = "chicken", required = false) Cookie cookie,
                                        @Valid @RequestBody CartRequestDto addCartRequestDTO,
                                        HttpServletResponse response) {
        UpdatableCartDto updatableCartDto = getUpdatableCartDto(cookie);

        Cookie responseCookie = cartUseCase.addCart(updatableCartDto, addCartRequestDTO).getCookie();

        addCookie(response, responseCookie);

        return ResponseEntity.ok().build();
    }

    /**
     * 장바구니 상품 수정
     *
     * @param cookie         쿠키
     * @param cartRequestDTO 수정할 상품
     * @param response       response
     */
    @PutMapping
    public ResponseEntity<Void> modifyCart(@CookieValue(value = "chicken", required = false) Cookie cookie,
                                           @Valid @RequestBody CartRequestDto cartRequestDTO,
                                           HttpServletResponse response) {
        UpdatableCartDto updatableCartDto = getUpdatableCartDto(cookie);

        Cookie responseCookie = cartUseCase.modifyCart(updatableCartDto, cartRequestDTO).getCookie();

        addCookie(response, responseCookie);

        return ResponseEntity.ok().build();
    }

    /**
     * 장바구니 상품 삭제
     *
     * @param cookie         쿠키
     * @param cartRequestDTO 삭제할 상품
     * @param response       response
     */
    @DeleteMapping
    public ResponseEntity<Void> removeCart(@CookieValue(value = "chicken", required = false) Cookie cookie,
                                           @Valid @RequestBody CartRequestDto cartRequestDTO,
                                           HttpServletResponse response) {
        UpdatableCartDto updatableCartDto = getUpdatableCartDto(cookie);

        Cookie responseCookie = cartUseCase.removeCart(updatableCartDto, cartRequestDTO).getCookie();

        addCookie(response, responseCookie);

        return ResponseEntity.ok().build();
    }

    private void addCookie(HttpServletResponse response, Cookie responseCookie) {
        response.addCookie(responseCookie);
    }

    private UpdatableCartDto getUpdatableCartDto(Cookie cookie) {
        return UpdatableCartDto.builder()
                .cookie(cookie)
                .build();
    }
}
