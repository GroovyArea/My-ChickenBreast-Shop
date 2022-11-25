package com.daniel.mychickenbreastshop.domain.order.model.query;

import com.daniel.mychickenbreastshop.domain.order.model.Order;
import com.daniel.mychickenbreastshop.domain.order.model.OrderProduct;
import com.daniel.mychickenbreastshop.domain.order.model.OrderRepository;
import com.daniel.mychickenbreastshop.domain.order.model.enums.OrderStatus;
import com.daniel.mychickenbreastshop.domain.payment.model.Payment;
import com.daniel.mychickenbreastshop.domain.payment.model.enums.PayStatus;
import com.daniel.mychickenbreastshop.domain.payment.model.enums.PaymentType;
import com.daniel.mychickenbreastshop.domain.user.model.User;
import com.daniel.mychickenbreastshop.domain.user.model.UserRepository;
import com.daniel.mychickenbreastshop.domain.user.model.enums.Role;
import com.daniel.mychickenbreastshop.global.config.QuerydslConfig;
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

@DataJpaTest
@Import(QuerydslConfig.class)
class OrderCustomQueryRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        List<OrderProduct> orderProducts = new ArrayList<>();

        User user = User.builder()
                .id(1L)
                .loginId("loginId")
                .email("email")
                .role(Role.ROLE_USER)
                .build();

        User savedUser = userRepository.save(user);

        List<Order> savedOrders = new ArrayList<>();

        for (int i = 1; i < 50; i++) {
            Payment payment = Payment.builder()
                    .totalPrice(100000L)
                    .paymentType(PaymentType.CASH)
                    .status(PayStatus.COMPLETED)
                    .build();

            Order order = Order.builder()
                    .id((long) i)
                    .totalCount(10)
                    .orderPrice(100000L)
                    .status(OrderStatus.ORDER_COMPLETE)
                    .user(savedUser)
                    .orderProducts(orderProducts)
                    .payment(payment)
                    .build();

            savedOrders.add(orderRepository.save(order));
        }

        for (int i = 1; i < 10; i++) {
            OrderProduct orderProduct = OrderProduct.builder()
                    .name("name" + i)
                    .image("image" + i)
                    .count(5)
                    .price(10000)
                    .build();

            savedOrders.forEach(order -> order.addOrderProduct(orderProduct));
        }
    }

    @DisplayName("회원 번호에 해당하는 주문 정보를 페이징 반환한다.")
    @Test
    void findAllByUserId() {
        // given
        int page = 1;
        Pageable pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        Long userId = 1L;
        OrderStatus orderStatus = OrderStatus.ORDER_COMPLETE;

        // when
        Page<Order> orderPage = orderRepository.findAllByUserId(userId, orderStatus, pageRequest);
        List<Order> content = orderPage.getContent();

        assertThat(content).hasSize(10);
        content.forEach(order -> assertThat(order.getStatus()).isEqualTo(orderStatus));
        content.forEach(order -> assertThat(order.getPayment().getTotalPrice()).isEqualTo(100000L));
        content.forEach(order -> assertThat(order.getPayment().getPaymentType()).isEqualTo(PaymentType.CASH));
        content.forEach(order -> order.getOrderProducts().forEach(orderProduct -> assertThat(orderProduct.getName()).contains("name")));
    }

    @DisplayName("주문 번호를 가지고 결제 내역 정보와 Fetch Join 한 결과를 반환한다.")
    @Test
    void findByIdWithFetchJoin() {
        // given
        Long orderId = 1L;

        // when
        Order savedOrder = orderRepository.findByIdWithFetchJoin(orderId).orElseThrow(() -> new RuntimeException("주문 정보 없음"));

        assertThat(savedOrder.getPayment().getPaymentType()).isEqualTo(PaymentType.CASH);
        assertThat(savedOrder.getPayment().getStatus()).isEqualTo(PayStatus.COMPLETED);
        assertThat(savedOrder.getPayment().getTotalPrice()).isEqualTo(100000L);
    }
}