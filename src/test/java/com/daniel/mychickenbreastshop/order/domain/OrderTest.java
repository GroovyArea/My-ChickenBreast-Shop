package com.daniel.mychickenbreastshop.order.domain;

import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import com.daniel.mychickenbreastshop.order.domain.enums.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class OrderTest {

    @DisplayName("결제까지 완료된 주문 건을 취소할 경우 예외를 발생시킨다")
    @Test
    void cancelOrder(){
        // given
        Order order = Order.builder()
                .id(1L)
                .paymentId(1L)
                .status(OrderStatus.ORDER_COMPLETE)
                .build();

        // when & then
        assertThatThrownBy(order::cancelOrder)
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("이미 결제된 주문 건에 대해 취소가 불가능합니다.");
    }
}