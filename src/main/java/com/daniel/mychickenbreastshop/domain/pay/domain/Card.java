package com.daniel.mychickenbreastshop.domain.pay.domain;

import com.daniel.mychickenbreastshop.domain.order.domain.Order;
import com.daniel.mychickenbreastshop.global.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "cardinfo")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private String bin;

    private String cardType;

    private String installMonth;

    private String interestFreeInstall;

    private LocalDateTime deletedAt;
}
