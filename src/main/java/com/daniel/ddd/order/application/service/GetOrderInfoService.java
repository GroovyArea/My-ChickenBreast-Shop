package com.daniel.ddd.order.application.service;

import com.daniel.ddd.global.error.exception.BadRequestException;
import com.daniel.ddd.order.adaptor.out.persistence.OrderRepository;
import com.daniel.ddd.order.application.port.in.GetOrderInfoUseCase;
import com.daniel.ddd.order.domain.Order;
import com.daniel.ddd.order.domain.enums.ErrorMessages;
import com.daniel.ddd.order.domain.enums.OrderStatus;
import com.daniel.ddd.order.mapper.OrderInfoListMapper;
import com.daniel.ddd.order.mapper.OrderItemsInfoMapper;
import com.daniel.ddd.order.mapper.OrderPaymentInfoMapper;
import com.daniel.ddd.order.mapper.OrderProductsMapper;
import com.daniel.ddd.order.model.dto.response.OrderInfoListResponseDto;
import com.daniel.ddd.order.model.dto.response.OrderItemsInfoResponseDto;
import com.daniel.ddd.order.model.dto.response.OrderPaymentInfoResponseDto;
import com.daniel.ddd.order.model.dto.response.OrderProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetOrderInfoService implements GetOrderInfoUseCase {

    private final OrderRepository orderRepository;
    private final OrderInfoListMapper orderInfoListMapper;
    private final OrderItemsInfoMapper orderItemsInfoMapper;
    private final OrderProductsMapper orderProductListMapper;
    private final OrderPaymentInfoMapper orderPaymentInfoMapper;

    @Override
    public List<OrderInfoListResponseDto> getAllOrders(Long userId, OrderStatus orderStatus, Pageable pageable) {
        List<Order> orders = orderRepository.findAllByUserId(userId, orderStatus, pageable).getContent();

        return orders.stream()
                .map(orderInfoListMapper::toDTO)
                .toList();
    }

    @Override
    public OrderItemsInfoResponseDto getOrderDetail(Long orderId) {
        Order order = orderRepository.findByIdWithOrderProductsUsingFetchJoin(orderId)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.ORDER_NOT_EXISTS.getMessage()));

        OrderItemsInfoResponseDto orderItemsInfoResponseDto = orderItemsInfoMapper.toDTO(order);

        List<OrderProductResponseDto> orderProductResponseDtos = order.getOrderProducts().stream()
                .map(orderProductListMapper::toDTO)
                .toList();

        orderItemsInfoResponseDto.updateOrderProducts(orderProductResponseDtos);

        return orderItemsInfoResponseDto;
    }

    @Override
    public OrderPaymentInfoResponseDto getPaymentDetail(Long orderId) {
        Order order = orderRepository.findByIdWithPaymentUsingFetchJoin(orderId)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.ORDER_NOT_EXISTS.getMessage()));

        return orderPaymentInfoMapper.toDTO(order);
    }
}
