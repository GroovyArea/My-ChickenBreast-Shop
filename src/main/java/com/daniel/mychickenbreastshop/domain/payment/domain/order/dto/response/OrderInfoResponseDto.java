package com.daniel.mychickenbreastshop.domain.payment.domain.order.dto.response;

import com.daniel.mychickenbreastshop.domain.payment.domain.order.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderInfoResponseDto {

    private Long orderId;
    private List<OrderProductResponseDto> orderInfoResponseDtos;
    private Integer totalCount;
    private Long orderPrice;
    private OrderStatus status;

    public void updateOrderProducts(List<OrderProductResponseDto> dtos) {
        orderInfoResponseDtos = dtos;
    }
}
