package com.daniel.mychickenbreastshop.order.application.service;

import com.daniel.mychickenbreastshop.order.adaptor.out.persistence.OrderRepository;
import com.daniel.mychickenbreastshop.order.domain.Order;
import com.daniel.mychickenbreastshop.order.domain.OrderProduct;
import com.daniel.mychickenbreastshop.order.domain.enums.OrderStatus;
import com.daniel.mychickenbreastshop.order.mapper.OrderInfoListMapper;
import com.daniel.mychickenbreastshop.order.mapper.OrderItemsInfoMapper;
import com.daniel.mychickenbreastshop.order.mapper.OrderProductsMapper;
import com.daniel.mychickenbreastshop.order.model.dto.response.OrderInfoListResponseDto;
import com.daniel.mychickenbreastshop.order.model.dto.response.OrderItemsInfoResponseDto;
import com.daniel.mychickenbreastshop.order.model.dto.response.OrderProductResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetOrderInfoServiceTest {

    @Mock
    private  OrderRepository orderRepository;

    @Mock
    private  OrderInfoListMapper orderInfoListMapper;

    @Mock
    private  OrderItemsInfoMapper orderItemsInfoMapper;

    @Mock
    private  OrderProductsMapper orderProductListMapper;

    @InjectMocks
    private GetOrderInfoService getOrderInfoService;

    private List<Order> orders;
    private List<OrderProduct> orderProducts;
    private List<OrderInfoListResponseDto> responseDtos;
    private List<OrderProductResponseDto> orderProductResponseDtos;

    @BeforeEach
    void setUp() {
        orders = new ArrayList<>();
        orderProducts = new ArrayList<>();
        responseDtos = new ArrayList<>();
        orderProductResponseDtos = new ArrayList<>();

        for (int i = 1; i <= 51; i++) {
            OrderProduct orderProduct = OrderProduct.builder()
                    .id((long) i)
                    .count(3)
                    .name("name" + i)
                    .price(12000)
                    .image("image" + i + ".jpg")
                    .build();

            orderProducts.add(orderProduct);
        }

        long userId = 1;
        long paymentId = 1;

            Order order = Order.builder()
                    .id(1L)
                    .totalCount(5)
                    .orderPrice(360000L)
                    .status(OrderStatus.ORDER_COMPLETE)
                    .orderProducts(orderProducts)
                    .userId(userId)
                    .paymentId(paymentId)
                    .build();

        for (int i = 1; i <= 50; i++) {
            orders.add(order);

            OrderInfoListResponseDto responseDto = OrderInfoListResponseDto.builder()
                    .orderId(i)
                    .totalCount(i)
                    .orderPrice(360000L)
                    .orderStatus(OrderStatus.ORDER_COMPLETE)
                    .build();

            responseDtos.add(responseDto);

            OrderProductResponseDto orderProductResponseDto = OrderProductResponseDto.builder()
                    .orderProductId(orderProducts.get(i).getId())
                    .count(orderProducts.get(i).getCount())
                    .name(orderProducts.get(i).getName())
                    .price(orderProducts.get(i).getPrice())
                    .image(orderProducts.get(i).getImage())
                    .build();

            orderProductResponseDtos.add(orderProductResponseDto);
        }
    }

    @DisplayName("회원의 주문 완료된 주문 목록들을 조회한다.")
    @Test
    void getAllOrders() {
        // given
        int pageNumber = 1;
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Order> page = new PageImpl<>(List.of(orders.get(0), orders.get(1), orders.get(2), orders.get(3), orders.get(4),
                orders.get(5), orders.get(6), orders.get(7), orders.get(8), orders.get(9)), pageable, 10);

        OrderStatus orderStatus = OrderStatus.ORDER_COMPLETE;
        Long userId = 1L;

        // when
        when(orderRepository.findAllByUserId(userId, orderStatus, pageable)).thenReturn(page);
        when(orderInfoListMapper.toDTO(any(Order.class))).thenReturn(responseDtos.get(0));

        assertThat(getOrderInfoService.getAllOrders(userId, orderStatus, pageable)).hasSize(10);
    }

    @DisplayName("주문 상세 내역을 조회한다.")
    @Test
    void getOrderDetail() {
        // given
        OrderItemsInfoResponseDto dto = OrderItemsInfoResponseDto.builder()
                .orderId(orders.get(0).getId())
                .orderInfoResponseDtos(orderProductResponseDtos)
                .totalCount(orders.get(0).getTotalCount())
                .orderPrice(orders.get(0).getOrderPrice())
                .orderStatus(orders.get(0).getStatus())
                .build();

        // when
        when(orderRepository.findByIdWithOrderProductsUsingFetchJoin(orders.get(0).getId())).thenReturn(Optional.ofNullable(orders.get(0)));
        when(orderItemsInfoMapper.toDTO(orders.get(0))).thenReturn(dto);
        when(orderProductListMapper.toDTO(any(OrderProduct.class))).thenReturn(orderProductResponseDtos.get(0));

        assertThat(getOrderInfoService.getOrderDetail(orders.get(0).getId())).isEqualTo(dto);
    }
}