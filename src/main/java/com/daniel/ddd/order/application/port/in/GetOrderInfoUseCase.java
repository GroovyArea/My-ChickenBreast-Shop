package com.daniel.ddd.order.application.port.in;

import com.daniel.ddd.order.domain.enums.OrderStatus;
import com.daniel.ddd.order.model.dto.response.OrderInfoListResponseDto;
import com.daniel.ddd.order.model.dto.response.OrderItemsInfoResponseDto;
import com.daniel.ddd.order.model.dto.response.OrderPaymentInfoResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GetOrderInfoUseCase {

    List<OrderInfoListResponseDto> getAllOrders(Long userId, OrderStatus orderStatus, Pageable pageable);

    OrderItemsInfoResponseDto getOrderDetail(Long orderId);

    OrderPaymentInfoResponseDto getPaymentDetail(Long orderId);
}
