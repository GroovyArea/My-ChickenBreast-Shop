package com.daniel.ddd.order.adaptor.in.web.rest;

import com.daniel.ddd.order.application.port.in.GetOrderInfoUseCase;
import com.daniel.ddd.order.domain.enums.OrderStatus;
import com.daniel.ddd.order.model.dto.response.OrderInfoListResponseDto;
import com.daniel.ddd.order.model.dto.response.OrderItemsInfoResponseDto;
import com.daniel.ddd.order.model.dto.response.OrderPaymentInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderInfoApiController {

    private final GetOrderInfoUseCase orderInfoUseCase;

    /**
     * 회원 개인 주문 내역 조회
     * 페이지 번호
     *
     * @param orderStatus 주문 상태
     * @return 주문 내역
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<OrderInfoListResponseDto>> getAllOrders(
            @PathVariable Long userId,
            @PageableDefault(page = 1, sort = "createdAt",
                    direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(defaultValue = "ORDER_COMPLETE") OrderStatus orderStatus) {
        return ResponseEntity.ok(orderInfoUseCase.getAllOrders(userId, orderStatus, pageable));
    }

    /**
     * 주문 상세 조회
     *
     * @param orderId 주문 아이디
     * @return 주문 상세 내역
     */
    @GetMapping("/details/{orderId}")
    public ResponseEntity<OrderItemsInfoResponseDto> getOrderInfo(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderInfoUseCase.getOrderDetail(orderId));
    }

    /**
     * 결제 내역 상세 조회
     *
     * @param orderId 주문 아이디
     * @return 결제 상세 내역
     */
    @GetMapping("/{orderId}/payments")
    public ResponseEntity<OrderPaymentInfoResponseDto> getPaymentInfo(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderInfoUseCase.getPaymentDetail(orderId));
    }

}
