package com.daniel.mychickenbreastshop.order.model.dto.response;

import com.daniel.mychickenbreastshop.order.domain.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemsInfoResponseDto {

    private Long orderId;
    private List<OrderProductResponseDto> orderInfoResponseDtos;
    private Integer totalCount;
    private Long orderPrice;
    private OrderStatus orderStatus;

    public void updateOrderProducts(List<OrderProductResponseDto> dtos) {
        orderInfoResponseDtos = dtos;
    }

}
