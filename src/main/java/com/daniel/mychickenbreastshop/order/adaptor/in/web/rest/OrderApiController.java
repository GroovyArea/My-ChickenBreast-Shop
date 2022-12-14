package com.daniel.mychickenbreastshop.order.adaptor.in.web.rest;

import com.daniel.mychickenbreastshop.global.util.PrincipalUtil;
import com.daniel.mychickenbreastshop.order.application.port.in.ManageOrderUseCase;
import com.daniel.mychickenbreastshop.order.model.dto.request.OrderRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 주문 api 컨트롤러
 * <p>
 * 주문 준비
 * 주문 취소
 * 주문 완료
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderApiController {

    private final ManageOrderUseCase orderUseCase;

    @PostMapping
    public ResponseEntity<Long> createOrder(@RequestBody List<OrderRequestDto> orderRequestDtos) {
        Long orderId = orderUseCase.makeOrderReady(orderRequestDtos, PrincipalUtil.getCurrentId());
        return ResponseEntity.ok().body(orderId);
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<Long> completeOrder(@PathVariable Long orderId) {
        orderUseCase.makeOrderComplete(orderId);
        return ResponseEntity.ok().body(orderId);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        orderUseCase.makeOrderCanceled(orderId);
        return ResponseEntity.ok().build();
    }

}
