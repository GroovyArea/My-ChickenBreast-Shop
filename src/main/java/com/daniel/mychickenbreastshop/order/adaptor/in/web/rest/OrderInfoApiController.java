package com.daniel.mychickenbreastshop.order.adaptor.in.web.rest;

import com.daniel.mychickenbreastshop.order.application.port.in.GetOrderInfoUseCase;
import com.daniel.mychickenbreastshop.order.domain.enums.OrderStatus;
import com.daniel.mychickenbreastshop.order.model.dto.response.OrderInfoListResponseDto;
import com.daniel.mychickenbreastshop.order.model.dto.response.OrderItemsInfoResponseDto;
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

    private final GetOrderInfoUseCase getOrderInfoUseCase;

    /**
     * 회원 개인 주문 내역 조회
     * 페이지 번호
     *
     * @param orderStatus 주문 상태
     * @return 주문 내역
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderInfoListResponseDto>> getAllOrders(
            @PathVariable Long userId,
            @PageableDefault(page = 1, sort = "createdAt",
                    direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(defaultValue = "ORDER_COMPLETE") OrderStatus orderStatus) {
        return ResponseEntity.ok(getOrderInfoUseCase.getAllOrders(userId, orderStatus, pageable));
    }

    /**
     * 주문 상세 조회
     *
     * @param orderId 주문 아이디
     * @return 주문 상세 내역
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderItemsInfoResponseDto> getOrderInfo(@PathVariable Long orderId) {
        return ResponseEntity.ok(getOrderInfoUseCase.getOrderDetail(orderId));
    }

}
