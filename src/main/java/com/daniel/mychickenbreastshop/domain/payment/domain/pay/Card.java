package com.daniel.mychickenbreastshop.domain.payment.domain.pay;

import com.daniel.mychickenbreastshop.global.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "card_info")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bin;

    @Column(name = "card_type", nullable = false)
    private String cardType;

    @Column(name = "install_month", nullable = false)
    private String installMonth;

    @Column(name = "interest_free_install", nullable = false)
    private String interestFreeInstall;

    @OneToOne(mappedBy = "card")
    private Payment payment;

    // <연관 관계 편의 메서드> //

    // <비즈니스 로직 메서드> //

    // <카드 생성 메서드> //
    public static Card createCard(String bin, String cardType, String installMonth, String interestFreeInstall) {
        return Card.builder()
                .bin(bin)
                .cardType(cardType)
                .installMonth(installMonth)
                .interestFreeInstall(interestFreeInstall)
                .build();
    }

    public void updatePaymentInfo(Payment payment) {
        this.payment = payment;
    }
}

