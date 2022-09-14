package com.daniel.mychickenbreastshop.domain.payment.api;

import com.daniel.mychickenbreastshop.domain.payment.application.OrderService;
import com.daniel.mychickenbreastshop.domain.payment.domain.order.dto.response.OrderDetailResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderApiController {

    private final OrderService orderService;

    /**
     * 회원 개인 주문 내역(DB) 조회
     */
    @GetMapping
    public ResponseEntity<List<OrderDetailResponseDto>> geOrderInfo(HttpServletRequest request, Pageable pageable) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(orderService.getOrderInfoData(userId, pageable));
    }

    /**
     * 주문 취소 요청
     */
    @PatchMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable("orderId") Long orderId,
                                            HttpServletRequest request) {
        Long userId = getUserId(request);
        orderService.cancelOrder(orderId, userId);
        return ResponseEntity.ok().build();
    }

    private Long getUserId(HttpServletRequest request) {
        return (Long) request.getAttribute("userId");
    }
}
