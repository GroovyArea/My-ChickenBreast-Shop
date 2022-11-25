package com.daniel.mychickenbreastshop.domain.payment.model;

import com.daniel.mychickenbreastshop.domain.order.model.Order;
import com.daniel.mychickenbreastshop.domain.payment.model.enums.PayStatus;
import com.daniel.mychickenbreastshop.domain.payment.model.enums.PaymentType;
import com.daniel.mychickenbreastshop.global.domain.BaseTimeEntity;
import lombok.*;

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
    @JoinColumn(name = "card_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Card card;

    // <팩토리 메서드> //
    public static Payment createPayment(long totalPrice) {
        return Payment.builder()
                .totalPrice(totalPrice)
                .status(PayStatus.READY)
                .card(null)
                .build();
    }

    // <연관관계 편의 메서드> //
    public void updateOrderInfo(final Order orderInfo) {
        this.order = orderInfo;
    }

    public void updateCardInfo(final Card cardInfo) {
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

    public void cancelPayment() {
        this.status = PayStatus.CANCELED;
        this.delete();
    }

}
