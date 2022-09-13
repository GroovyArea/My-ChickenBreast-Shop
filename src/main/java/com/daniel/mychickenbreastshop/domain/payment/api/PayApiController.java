package com.daniel.mychickenbreastshop.domain.payment.api;

import com.daniel.mychickenbreastshop.domain.order.domain.dto.request.ItemOrderRequestDto;
import com.daniel.mychickenbreastshop.domain.payment.application.PayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 결제 API
 * <pre>
 *     <b>history</b>
 *     1.0 2022.09.13 최초작성
 * </pre>
 *
 * @version 1.0
 * @author Daniel Kim
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pay")
public class PayApiController {

    private final PayService payService;



    /**
     * 단일 상품 결제 요청
     */
    @PostMapping
    public ResponseEntity<String> itemOrder(@RequestBody ItemOrderRequestDto itemOrderRequestDto,
                                            HttpServletRequest request) {
        Long userId = getUserId(request);
        String requestURL = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        String payableURL = payService.getSingleItemPayURL(itemOrderRequestDto, requestURL, userId);

        return ResponseEntity.ok(payableURL);
    }

    /**
     * 장바구니 상품 결제 요청
     */
    @PostMapping("/cart")
    public ResponseEntity<String> cartItemsOrder(HttpServletRequest request,
                                                 @CookieValue(value = "chicken") Cookie cookie) {
        Long userId = getUserId(request);
        String requestURL = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        String payableURL = payService.getCartItemsPayURL(cookie.getValue(), requestURL, userId);

        return ResponseEntity.ok(payableURL);
    }

    private Long getUserId(HttpServletRequest request) {
        return (Long) request.getAttribute("userId");
    }

}
