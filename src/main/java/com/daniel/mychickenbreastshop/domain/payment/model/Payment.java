package com.daniel.mychickenbreastshop.domain.payment.model;

import com.daniel.mychickenbreastshop.domain.payment.model.order.Order;
import com.daniel.mychickenbreastshop.domain.payment.model.model.PayStatus;
import com.daniel.mychickenbreastshop.domain.payment.model.model.PaymentType;
import com.daniel.mychickenbreastshop.global.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_price", nullable = false)
    private Long totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "pay_status", nullable = false)
    private PayStatus status;

    @OneToOne(mappedBy = "payment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Order order;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "card_id")
    private Card card;

    // <연관관계 편의 메서드> //

    public void updateOrderInfo(final Order orderInfo) {
        this.order = orderInfo;
    }

    public void setCardInfo(final Card cardInfo) {
        this.card = cardInfo;
        card.updatePaymentInfo(this);
    }


    // <비즈니스 로직 메서드> //

    public void updatePaymentTypeInfo(final PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public void updatePaymentStatus(final PayStatus status) {
        this.status = status;
    }

}
