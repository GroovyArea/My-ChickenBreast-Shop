package com.daniel.mychickenbreastshop.domain.order.api;

import com.daniel.mychickenbreastshop.domain.order.application.OrderService;
import com.daniel.mychickenbreastshop.domain.order.domain.dto.request.ItemOrderRequestDto;
import com.daniel.mychickenbreastshop.domain.order.domain.dto.response.OrderDetailResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    /**
     * 회원 개인 주문 내역 조회
     *
     * @param pageable
     * @return
     */
    @GetMapping
    public ResponseEntity<List<OrderDetailResponseDto>> getOrderInfo(HttpServletRequest request, Pageable pageable) {
        Long userId = (Long) request.getAttribute("userId");
        return ResponseEntity.ok(orderService.getOrderInfoData(userId, pageable));
    }

    /**
     * 단일 상품 주문 요청
     *
     * @param itemOrderRequestDto
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity<String> itemOrder(@RequestBody ItemOrderRequestDto itemOrderRequestDto,
                                            HttpServletRequest request) {
        String requestURL = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        String payableURL = orderService.getSingleItemPayURL(itemOrderRequestDto, requestURL);

        return ResponseEntity.ok(payableURL);
    }

    /**
     * 장바구니 상품 주문 요청
     *
     * @param request
     * @param cookie
     * @return
     */
    @PostMapping("/cart")
    public ResponseEntity<String> cartItemsOrder(HttpServletRequest request,
                                                 @CookieValue(value = "chicken") Cookie cookie) {
        String requestURL = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        String payableURL = orderService.getCartItemsPayURL(cookie.getValue(), requestURL);

        return ResponseEntity.ok(payableURL);
    }

    /**
     * 주문 취소 요청
     * @param orderId
     * @return
     */
    @PatchMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok().build();
    }
}
