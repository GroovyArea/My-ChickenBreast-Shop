package com.daniel.mychickenbreastshop.domain.pay.domain;

import com.daniel.mychickenbreastshop.domain.order.domain.Order;
import com.daniel.mychickenbreastshop.domain.pay.enums.PayStatus;
import com.daniel.mychickenbreastshop.domain.pay.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToOne(mappedBy = "payment")
    private Card card;

    private String key;

    @Column(updatable = false)
    private LocalDateTime readyAt;

    private LocalDateTime approvedAt;

    private LocalDateTime updatedAt;

    private LocalDateTime canceledAt;

    private int totalPrice;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private PayStatus status;

}
