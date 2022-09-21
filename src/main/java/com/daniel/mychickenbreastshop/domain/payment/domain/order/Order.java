package com.daniel.mychickenbreastshop.domain.payment.domain.order;

import com.daniel.mychickenbreastshop.domain.payment.domain.order.model.OrderStatus;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.Payment;
import com.daniel.mychickenbreastshop.domain.user.domain.User;
import com.daniel.mychickenbreastshop.global.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "ORDERS")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_count")
    private Integer totalCount;

    private Long orderPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<OrderProduct> orderProducts = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    // <연관관계 편의 메서드> //

    public void setUserInfo(final User userInfo) {
        if (this.user != null) {
            user.getOrders().remove(this);
        }

        this.user = userInfo;
        user.getOrders().add(this);
    }

    public void setPaymentInfo(final Payment paymentInfo) {
        this.payment = paymentInfo;
        payment.updateOrderInfo(this);
    }

    public void addOrderProduct(final OrderProduct orderProduct) {
        orderProducts.add(orderProduct);
        orderProduct.updateOrderInfo(this);
    }

    // <주문 생성 메서드> //

    public static Order createOrder(User user, Payment payment, List<OrderProduct> orderProducts) {
        return Order.builder()
                .user(user)
                .payment(payment)
                .orderProducts(orderProducts)
                .status(OrderStatus.ORDER_APPROVAL)
                .totalCount(orderProducts.size())
                .orderPrice(orderProducts.stream().map(OrderProduct::getPrice).mapToLong(Integer::longValue).sum())
                .build();
    }

    public static Order createReadyOrder(final int quantity, final long totalAmount, final User user) {
        return Order.builder()
                .totalCount(quantity)
                .orderPrice(totalAmount)
                .status(OrderStatus.ORDER_READY)
                .user(user)
                .build();
    }

    // <비즈니스 로직 메서드> //
    public void orderCancel() {
        this.updateOrderStatus(OrderStatus.CANCEL_ORDER);
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        this.status = orderStatus;
    }


}
