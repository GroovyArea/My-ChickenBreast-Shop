package com.daniel.mychickenbreastshop.domain.order.api;

import com.daniel.mychickenbreastshop.domain.order.application.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{userId}")
    public ResponseEntity<> getOrderInfo(@PathVariable Long userId) {
        List
        return ResponseEntity.ok(orderService.getOrderInfoData(userId)).build();
    }
}
