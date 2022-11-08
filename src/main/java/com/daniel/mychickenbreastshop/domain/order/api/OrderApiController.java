package com.daniel.mychickenbreastshop.domain.order.api;

import com.daniel.mychickenbreastshop.auth.security.model.PrincipalDetails;
import com.daniel.mychickenbreastshop.domain.order.application.OrderService;
import com.daniel.mychickenbreastshop.domain.order.model.dto.response.OrderInfoListResponseDto;
import com.daniel.mychickenbreastshop.domain.order.model.dto.response.OrderItemsInfoResponseDto;
import com.daniel.mychickenbreastshop.domain.order.model.dto.response.OrderPaymentInfoResponseDto;
import com.daniel.mychickenbreastshop.domain.order.model.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderApiController {

    private final OrderService orderService;

    /**
     * 회원 개인 주문 내역 조회
     *
     * @param page 페이지 번호
     * @param orderStatus 주문 상태
     * @return 주문 내역
     */
    @GetMapping("/v1/orders")
    public ResponseEntity<List<OrderInfoListResponseDto>> getAllOrders(@RequestParam(defaultValue = "1") int page,
                                                                       @RequestParam(defaultValue = "ORDER_COMPLETE") OrderStatus orderStatus) {
        Long userId = getUserId();
        return ResponseEntity.ok(orderService.getAllOrders(userId, orderStatus, page));
    }

    /**
     * 주문 상세 조회
     *
     * @param orderId 주문 아이디
     * @return 주문 상세 내역
     */
    @GetMapping("/v1/orders/detail/{orderId}")
    public ResponseEntity<OrderItemsInfoResponseDto> getOrderInfo(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderDetail(orderId));
    }

    /**
     * 결제 내역 상세 조회
     *
     * @param orderId 주문 아이디
     * @return 결제 상세 내역
     */
    @GetMapping("/v1/orders/payment/{orderId}")
    public ResponseEntity<OrderPaymentInfoResponseDto> getPaymentInfo(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getPaymentDetail(orderId));
    }

    private Long getUserId() {
        PrincipalDetails principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principalDetails.getId();
    }
}
