package com.daniel.mychickenbreastshop.domain.order.domain;

import com.daniel.mychickenbreastshop.domain.order.enums.OrderStatus;
import com.daniel.mychickenbreastshop.domain.pay.domain.Card;
import com.daniel.mychickenbreastshop.domain.pay.domain.Payment;
import com.daniel.mychickenbreastshop.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "order")
    private Payment payment;

    @OneToOne(mappedBy = "order")
    private Card card;

    @OneToMany(mappedBy = "orderedProduct")
    private List<OrderedProduct> orderedProducts = new ArrayList<>();

    private int count;

    private int orderPrice;

    @Column(updatable = false)
    private LocalDateTime orderedAt;

    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        orderedAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
