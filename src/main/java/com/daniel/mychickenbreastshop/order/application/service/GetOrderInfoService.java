package com.daniel.mychickenbreastshop.order.application.service;

import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import com.daniel.mychickenbreastshop.order.adaptor.out.persistence.OrderRepository;
import com.daniel.mychickenbreastshop.order.application.port.in.GetOrderInfoUseCase;
import com.daniel.mychickenbreastshop.order.domain.Order;
import com.daniel.mychickenbreastshop.order.domain.enums.ErrorMessages;
import com.daniel.mychickenbreastshop.order.domain.enums.OrderStatus;
import com.daniel.mychickenbreastshop.order.mapper.OrderInfoListMapper;
import com.daniel.mychickenbreastshop.order.mapper.OrderItemsInfoMapper;
import com.daniel.mychickenbreastshop.order.mapper.OrderProductsMapper;
import com.daniel.mychickenbreastshop.order.model.dto.response.OrderInfoListResponseDto;
import com.daniel.mychickenbreastshop.order.model.dto.response.OrderItemsInfoResponseDto;
import com.daniel.mychickenbreastshop.order.model.dto.response.OrderProductResponseDto;
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

}
