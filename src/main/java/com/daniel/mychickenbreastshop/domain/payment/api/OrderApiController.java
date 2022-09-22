package com.daniel.mychickenbreastshop.domain.payment.api;

import com.daniel.mychickenbreastshop.domain.payment.application.OrderService;
import com.daniel.mychickenbreastshop.domain.payment.domain.order.dto.response.OrderProductResponseDto;
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
    public ResponseEntity<List<OrderProductResponseDto>> geOrderInfo(HttpServletRequest request, Pageable pageable) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(orderService.getOrderInfoData(userId, pageable));
    }

    private Long getUserId(HttpServletRequest request) {
        return (Long) request.getAttribute("userId");
    }
}
