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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.daniel.mychickenbreastshop.domain.order.model.enums.OrderResponse.ORDER_NOT_EXISTS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderInfoListMapper orderInfoListMapper;
    private final OrderItemsInfoMapper orderItemsInfoMapper;
    private final OrderProductListMapper orderProductListMapper;
    private final OrderPaymentInfoMapper orderPaymentInfoMapper;

    public List<OrderInfoListResponseDto> getAllOrders(Long userId, OrderStatus orderStatus, int page) {
        PageRequest pageRequest = createPageRequest(page);
        List<Order> orders = orderRepository.findAllByUserId(userId, orderStatus, pageRequest).getContent();

        return orders.stream()
                .map(orderInfoListMapper::toDTO)
                .toList();
    }

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

    public OrderPaymentInfoResponseDto getPaymentDetail(Long orderId) {
        Order order = orderRepository.findByIdWithFetchJoin(orderId).orElseThrow(() -> new BadRequestException(ORDER_NOT_EXISTS.getMessage()));
        return orderPaymentInfoMapper.toDTO(order);
    }

    private PageRequest createPageRequest(int page) {
        return PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
    }
}
