package com.daniel.mychickenbreastshop.order.application.port.in;

import com.daniel.mychickenbreastshop.order.domain.enums.OrderStatus;
import com.daniel.mychickenbreastshop.order.model.dto.response.OrderInfoListResponseDto;
import com.daniel.mychickenbreastshop.order.model.dto.response.OrderItemsInfoResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GetOrderInfoUseCase {

    List<OrderInfoListResponseDto> getAllOrders(Long userId, OrderStatus orderStatus, Pageable pageable);

    OrderItemsInfoResponseDto getOrderDetail(Long orderId);

}
