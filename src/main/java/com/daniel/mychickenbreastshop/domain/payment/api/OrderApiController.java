package com.daniel.mychickenbreastshop.domain.payment.api;

import com.daniel.mychickenbreastshop.domain.payment.application.OrderService;
import com.daniel.mychickenbreastshop.domain.payment.domain.order.dto.response.OrderInfoListResponseDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.order.dto.response.OrderInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderApiController {

    private final OrderService orderService;

    /**
     * 회원 개인 주문 내역 조회
     * @param page 페이지 번호
     * @return 주문 내역
     */
    @GetMapping("/v1/orders")
    public ResponseEntity<List<OrderInfoListResponseDto>> getAllOrders(HttpServletRequest request,
                                                                      @RequestParam(defaultValue = "1") int page) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(orderService.getAllOrders(userId, page));
    }

    /**
     * 주문 상세 조회
     * @param orderId 주문 아이디
     * @return 주문 상세 내역
     */
    @GetMapping("/v1/orders/detail/{orderId}")
    public ResponseEntity<OrderInfoResponseDto> getOrderInfo(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderInfo(orderId));
    }

    private Long getUserId(HttpServletRequest request) {
        return (Long) request.getAttribute("userId");
    }
}
