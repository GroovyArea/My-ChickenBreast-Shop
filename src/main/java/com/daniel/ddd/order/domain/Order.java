package com.daniel.ddd.order.domain;

import com.daniel.ddd.global.domain.BaseTimeEntity;
import com.daniel.ddd.global.error.exception.BadRequestException;
import com.daniel.ddd.order.domain.enums.ErrorMessages;
import com.daniel.ddd.order.domain.enums.OrderStatus;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "ORDERS")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Order extends BaseTimeEntity<Order> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_count")
    private Integer totalCount;

    @Column(name = "order_price")
    private Long orderPrice;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "payment_id")
    private Long paymentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts = new ArrayList<>();


    // <연관관계 편의 메서드> //

    public void updatePaymentInfo(final long paymentId) {
        this.paymentId = paymentId;
    }

    public void addOrderProduct(final OrderProduct orderProduct) {
        orderProducts.add(orderProduct);
        orderProduct.updateOrderInfo(this);
    }

    // <주문 생성 메서드> //

    public static Order createReadyOrder(final int quantity, final long totalAmount, final long userId) {
        return Order.builder()
                .totalCount(quantity)
                .orderPrice(totalAmount)
                .orderProducts(new ArrayList<>())
                .status(OrderStatus.ORDER_READY)
                .userId(userId)
                .build();
    }

    public static Order createOrder(final List<OrderProduct> orderProducts, final long userId) {
        return Order.builder()
                .totalCount(orderProducts.size())
                .orderPrice(orderProducts.stream().mapToLong(OrderProduct::getPrice).sum())
                .status(OrderStatus.ORDER_READY)
                .userId(userId)
                .orderProducts(orderProducts)
                .build();
    }

    // <비즈니스 로직 메서드> //

    public void cancelOrder() {
        if (this.paymentId != null) {
            throw new BadRequestException(ErrorMessages.CANNOT_CANCEL_ORDER.getMessage());
        }

        this.status = OrderStatus.CANCEL_ORDER;
        this.delete();
    }

    public void completeOrder() {
        this.status = OrderStatus.ORDER_COMPLETE;
    }
}
