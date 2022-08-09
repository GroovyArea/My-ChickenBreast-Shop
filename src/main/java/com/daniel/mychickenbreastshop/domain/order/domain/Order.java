package com.daniel.mychickenbreastshop.domain.order.domain;

import com.daniel.mychickenbreastshop.domain.order.enums.OrderStatus;
import com.daniel.mychickenbreastshop.domain.pay.domain.Card;
import com.daniel.mychickenbreastshop.domain.pay.domain.Payment;
import com.daniel.mychickenbreastshop.domain.user.domain.User;
import com.daniel.mychickenbreastshop.global.domain.BaseTimeEntity;
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
public class Order extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "order")
    private Payment payment;

    @OneToOne(mappedBy = "order")
    private Card card;
`
    @OneToMany(mappedBy = "order")
    private List<OrderedProduct> orderedProducts = new ArrayList<>();

    private int count;

    private int orderPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

}
