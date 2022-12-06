package com.daniel.mychickenbreastshop.domain.order.model;

import com.daniel.mychickenbreastshop.domain.order.model.enums.OrderStatus;
import com.daniel.mychickenbreastshop.domain.payment.model.Payment;
import com.daniel.mychickenbreastshop.domain.user.model.User;
import com.daniel.mychickenbreastshop.global.domain.BaseTimeEntity;
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

    private Long orderPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<OrderProduct> orderProducts = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Payment payment;

    // <연관관계 편의 메서드> //

    public void updateUserInfo(final User userInfo) {
        if (this.user != null) {
            user.getOrders().remove(this);
        }

        this.user = userInfo;
        user.getOrders().add(this);
    }

    public void updatePaymentInfo(final Payment paymentInfo) {
        this.payment = paymentInfo;
        payment.updateOrderInfo(this);
    }

    public void addOrderProduct(final OrderProduct orderProduct) {
        orderProducts.add(orderProduct);
        orderProduct.updateOrderInfo(this);
    }

    // <주문 생성 메서드> //

    public static Order createReadyOrder(final int quantity, final long totalAmount, final User user) {
        return Order.builder()
                .totalCount(quantity)
                .orderPrice(totalAmount)
                .orderProducts(new ArrayList<>())
                .status(OrderStatus.ORDER_READY)
                .user(user)
                .build();
    }

    // <비즈니스 로직 메서드> //

    public void cancelOrder() {
        this.updateOrderStatus(OrderStatus.CANCEL_ORDER);
        this.delete();
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        this.status = orderStatus;
    }
}
