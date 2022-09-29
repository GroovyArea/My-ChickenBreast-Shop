package com.daniel.mychickenbreastshop.domain.payment.application;

import com.daniel.mychickenbreastshop.domain.payment.domain.order.Order;
import com.daniel.mychickenbreastshop.domain.payment.domain.order.OrderProduct;
import com.daniel.mychickenbreastshop.domain.payment.domain.order.OrderRepository;
import com.daniel.mychickenbreastshop.domain.payment.domain.order.dto.response.OrderInfoListResponseDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.order.dto.response.OrderInfoResponseDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.order.dto.response.OrderProductResponseDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.order.model.OrderResponse;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.response.PaymentInfoResponseDto;
import com.daniel.mychickenbreastshop.domain.payment.mapper.order.OrderInfoListMapper;
import com.daniel.mychickenbreastshop.domain.payment.mapper.order.OrderInfoMapper;
import com.daniel.mychickenbreastshop.domain.payment.mapper.order.OrderProductListMapper;
import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.daniel.mychickenbreastshop.domain.payment.domain.order.model.OrderResponse.ORDER_NOT_EXISTS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderInfoListMapper orderInfoListMapper;
    private final OrderInfoMapper orderInfoMapper;
    private final OrderProductListMapper orderProductListMapper;

    public List<OrderInfoListResponseDto> getAllOrders(Long userId, int page) {
        PageRequest pageRequest = createPageRequest(page);
        List<Order> orders = orderRepository.findAllByUserId(pageRequest, userId).getContent();

        return orders.stream()
                .map(orderInfoListMapper::toDTO)
                .toList();
    }

    public OrderInfoResponseDto getOrderDetail(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new BadRequestException(ORDER_NOT_EXISTS.getMessage()));
        List<OrderProduct> orderProducts = order.getOrderProducts();
        OrderInfoResponseDto orderInfoResponseDto = orderInfoMapper.toDTO(order);
        List<OrderProductResponseDto> orderProductResponseDtos = orderProducts.stream()
                .map(orderProductListMapper::toDTO)
                .toList();
        orderInfoResponseDto.updateOrderProducts(orderProductResponseDtos);

        return orderInfoResponseDto;
    }

    private PageRequest createPageRequest(int page) {
        return PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "created_at"));
    }

    public PaymentInfoResponseDto getPaymentDetail(Long orderId) {
        Order order = orderRepository.findByIdWithFetchJoin(orderId).orElseThrow(ORDER_NOT_EXISTS.getMessage());

    }
}
