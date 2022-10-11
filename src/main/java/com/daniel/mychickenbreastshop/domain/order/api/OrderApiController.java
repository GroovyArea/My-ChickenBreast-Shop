package com.daniel.mychickenbreastshop.domain.order.api;

import com.daniel.mychickenbreastshop.domain.order.application.OrderService;
import com.daniel.mychickenbreastshop.domain.payment.model.order.dto.response.OrderInfoListResponseDto;
import com.daniel.mychickenbreastshop.domain.payment.model.order.dto.response.OrderItemsInfoResponseDto;
import com.daniel.mychickenbreastshop.domain.payment.model.order.dto.response.OrderPaymentInfoResponseDto;
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
    public ResponseEntity<OrderItemsInfoResponseDto> getOrderInfo(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderDetail(orderId));
    }

    /**
     * 결제 내역 상세 조회
     * @param orderId 주문 아이디
     * @return 결제 상세 내역
     */
    @GetMapping("/v1/orders/payment/{orderId}")
    public ResponseEntity<OrderPaymentInfoResponseDto> getPaymentInfo(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getPaymentDetail(orderId));
    }

    private Long getUserId(HttpServletRequest request) {
        return (Long) request.getAttribute("userId");
    }
}
