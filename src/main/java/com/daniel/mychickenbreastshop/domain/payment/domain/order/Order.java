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

    private Integer orderPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<OrderProduct> orderedProducts = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    public void updateUserInfo(final User updatableUser) {
        if (this.user != null) {
            this.user.getOrders().remove(this);
        }

        this.user = updatableUser;
        this.user.getOrders().add(this);
    }

}
