package com.daniel.mychickenbreastshop.order.application.service;

import com.daniel.mychickenbreastshop.global.aspect.annotation.RedisLocked;
import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import com.daniel.mychickenbreastshop.global.event.builder.EventBuilder;
import com.daniel.mychickenbreastshop.order.adaptor.out.persistence.OrderRepository;
import com.daniel.mychickenbreastshop.order.application.port.in.ManageOrderUseCase;
import com.daniel.mychickenbreastshop.order.application.port.out.event.model.OrderValidation;
import com.daniel.mychickenbreastshop.order.domain.Order;
import com.daniel.mychickenbreastshop.order.domain.OrderProduct;
import com.daniel.mychickenbreastshop.order.domain.enums.ErrorMessages;
import com.daniel.mychickenbreastshop.order.mapper.OrderProductsMapper;
import com.daniel.mychickenbreastshop.order.model.dto.request.OrderRequestDto;
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
    private final ApplicationEventPublisher eventPublisher;
    private final EventBuilder<OrderValidation> eventBuilder;

    @Override
    @RedisLocked(lockKey = "order-items-lock")
    public Long makeOrderReady(List<OrderRequestDto> orderRequestDtos, Long userId) {
        List<OrderProduct> orderProducts = orderRequestDtos.stream()
                .map(orderProductsMapper::toEntity)
                .toList();

        orderRequestDtos.forEach(orderRequestDto ->
                eventPublisher.publishEvent(
                        eventBuilder.createEvent(new OrderValidation(
                                orderRequestDto.getItemNumber(),
                                orderRequestDto.getQuantity(),
                                orderRequestDto.getTotalAmount()
                        ))
                )
        );

        Order order = Order.createOrder(orderProducts, userId);
        orderProducts.forEach(order::addOrderProduct);

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
