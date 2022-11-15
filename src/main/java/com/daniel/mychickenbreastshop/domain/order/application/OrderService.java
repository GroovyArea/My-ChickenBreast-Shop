package com.daniel.mychickenbreastshop.domain.order.application;

import com.daniel.mychickenbreastshop.domain.order.mapper.OrderInfoListMapper;
import com.daniel.mychickenbreastshop.domain.order.mapper.OrderItemsInfoMapper;
import com.daniel.mychickenbreastshop.domain.order.mapper.OrderPaymentInfoMapper;
import com.daniel.mychickenbreastshop.domain.order.mapper.OrderProductListMapper;
import com.daniel.mychickenbreastshop.domain.order.model.Order;
import com.daniel.mychickenbreastshop.domain.order.model.OrderProduct;
import com.daniel.mychickenbreastshop.domain.order.model.OrderRepository;
import com.daniel.mychickenbreastshop.domain.order.model.dto.response.OrderInfoListResponseDto;
import com.daniel.mychickenbreastshop.domain.order.model.dto.response.OrderItemsInfoResponseDto;
import com.daniel.mychickenbreastshop.domain.order.model.dto.response.OrderPaymentInfoResponseDto;
import com.daniel.mychickenbreastshop.domain.order.model.dto.response.OrderProductResponseDto;
import com.daniel.mychickenbreastshop.domain.order.model.enums.OrderStatus;
import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.daniel.mychickenbreastshop.domain.order.model.enums.ErrorMessages.ORDER_NOT_EXISTS;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderInfoListMapper orderInfoListMapper;
    private final OrderItemsInfoMapper orderItemsInfoMapper;
    private final OrderProductListMapper orderProductListMapper;
    private final OrderPaymentInfoMapper orderPaymentInfoMapper;

    @Transactional(readOnly = true)
    public List<OrderInfoListResponseDto> getAllOrders(Long userId, OrderStatus orderStatus, Pageable pageable) {
        List<Order> orders = orderRepository.findAllByUserId(userId, orderStatus, pageable).getContent();

        return orders.stream()
                .map(orderInfoListMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderItemsInfoResponseDto getOrderDetail(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new BadRequestException(ORDER_NOT_EXISTS.getMessage()));
        List<OrderProduct> orderProducts = order.getOrderProducts();

        OrderItemsInfoResponseDto orderItemsInfoResponseDto = orderItemsInfoMapper.toDTO(order);
        List<OrderProductResponseDto> orderProductResponseDtos = orderProducts.stream()
                .map(orderProductListMapper::toDTO)
                .toList();

        orderItemsInfoResponseDto.updateOrderProducts(orderProductResponseDtos);

        return orderItemsInfoResponseDto;
    }

    @Transactional(readOnly = true)
    public OrderPaymentInfoResponseDto getPaymentDetail(Long orderId) {
        Order order = orderRepository.findByIdWithFetchJoin(orderId).orElseThrow(() -> new BadRequestException(ORDER_NOT_EXISTS.getMessage()));
        return orderPaymentInfoMapper.toDTO(order);
    }

}
