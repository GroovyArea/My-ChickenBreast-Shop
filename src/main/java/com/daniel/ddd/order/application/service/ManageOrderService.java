package com.daniel.ddd.order.application.service;

import com.daniel.ddd.global.aspect.annotation.RedisLocked;
import com.daniel.ddd.global.error.exception.BadRequestException;
import com.daniel.ddd.order.adaptor.out.persistence.OrderRepository;
import com.daniel.ddd.order.application.port.in.ManageOrderUseCase;
import com.daniel.ddd.order.domain.Order;
import com.daniel.ddd.order.domain.OrderProduct;
import com.daniel.ddd.order.domain.enums.ErrorMessages;
import com.daniel.ddd.order.mapper.OrderProductsMapper;
import com.daniel.ddd.order.model.dto.request.OrderRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ManageOrderService implements ManageOrderUseCase {

    private final OrderRepository orderRepository;
    private final OrderProductsMapper orderProductsMapper;
    private final ApplicationEventPublisher eventPublisher; // 상품 수량 확인 이벤트 갈겨라

    @Override
    @RedisLocked(lockKey = "order-items-lock")
    public Long makeOrderReady(List<OrderRequestDto> orderRequestDtos, Long userId) {
        List<OrderProduct> orderProducts = orderRequestDtos.stream()
                .map(orderProductsMapper::toEntity)
                .toList();

        Order order = Order.createOrder(orderProducts, userId);

        return orderRepository.save(order).getId();
    }

    @Override
    public void makeOrderComplete(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.ORDER_NOT_EXISTS.getMessage()));
        order.completeOrder();
    }

    @Override
    public void makeOrderCanceled(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.ORDER_NOT_EXISTS.getMessage()));
        order.cancelOrder();
    }
}
