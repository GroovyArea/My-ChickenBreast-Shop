package com.daniel.mychickenbreastshop.order.adaptor.out.persistence.query;

import com.daniel.mychickenbreastshop.global.config.QuerydslConfig;
import com.daniel.mychickenbreastshop.order.adaptor.out.persistence.OrderRepository;
import com.daniel.mychickenbreastshop.order.domain.Order;
import com.daniel.mychickenbreastshop.order.domain.OrderProduct;
import com.daniel.mychickenbreastshop.order.domain.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(QuerydslConfig.class)
class OrderCustomQueryRepositoryImplTest {

    @Autowired
    private OrderRepository orderRepository;


    @BeforeEach
    void setUp() {
        List<Order> orders = new ArrayList<>();
        List<OrderProduct> orderProducts = new ArrayList<>();

        long userId = 1;

        for (int i = 0; i < 10; i++) {
            OrderProduct orderProduct = OrderProduct.builder()
                    .name("name" + i)
                    .image("image" + i)
                    .count(5)
                    .price(10000)
                    .order(null)
                    .build();

            orderProducts.add(orderProduct);
        }

        for (int i = 0; i < 50; i++) {
            Order order = Order.builder()
                    .totalCount(10)
                    .orderPrice(100000L)
                    .status(OrderStatus.ORDER_COMPLETE)
                    .userId(userId)
                    .orderProducts(orderProducts)
                    .build();

            orders.add(order);

            orderRepository.save(order);
        }
    }

    @DisplayName("회원 번호에 해당하는 주문 정보를 페이징 반환한다.")
    @Test
    void findAllByUserId() {
        // given
        int page = 1;
        Pageable pageRequest = PageRequest.of
                (page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        Long userId = 1L;
        OrderStatus orderStatus = OrderStatus.ORDER_COMPLETE;

        // when
        Page<Order> orderPage = orderRepository.findAllByUserId(userId, orderStatus, pageRequest);
        List<Order> content = orderPage.getContent();

        content.forEach(order -> assertThat(order.getStatus()).isEqualTo(orderStatus));
        content.forEach(order -> order.getOrderProducts()
                .forEach(orderProduct -> assertThat(orderProduct.getName()).contains("name")));
    }

}