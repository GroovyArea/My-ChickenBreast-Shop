package com.daniel.mychickenbreastshop.payment.domain;

import com.daniel.mychickenbreastshop.global.domain.BaseTimeEntity;
import com.daniel.mychickenbreastshop.payment.domain.enums.PayStatus;
import com.daniel.mychickenbreastshop.payment.domain.enums.PaymentType;
import lombok.*;

import javax.persistence.*;

@Table(indexes = {
        @Index(name = "idx__order_id__status", columnList = "order_id, pay_status", unique = true)
})
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Payment extends BaseTimeEntity<Payment> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "total_price", nullable = false)
    private Long totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "pay_status", nullable = false)
    private PayStatus status;


    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "card_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Card card;

    // <팩토리 메서드> //
    public static Payment createPayment(final long orderId, final long totalPrice) {
        return Payment.builder()
                .orderId(orderId)
                .totalPrice(totalPrice)
                .status(PayStatus.READY)
                .card(null)
                .build();
    }

    // <연관관계 편의 메서드> //
    public void updateOrderInfo(final long orderId) {
        this.orderId = orderId;
    }

    public void updateCardInfo(final Card cardInfo) {
        this.card = cardInfo;
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
