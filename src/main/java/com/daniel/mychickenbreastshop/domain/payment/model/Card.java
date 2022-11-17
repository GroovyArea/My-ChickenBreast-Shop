package com.daniel.mychickenbreastshop.domain.payment.model;

import com.daniel.mychickenbreastshop.global.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Card extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bin;

    @Column(name = "card_type")
    private String cardType;

    @Column(name = "install_month")
    private String installMonth;

    @Column(name = "interest_free_install")
    private String interestFreeInstall;

    @OneToOne(mappedBy = "card")
    private Payment payment;

    // <연관 관계 편의 메서드> //
    public void updatePaymentInfo(Payment payment) {
        this.payment = payment;
    }

    // <카드 생성 메서드> //
    public static Card createCard(String bin, String cardType, String installMonth, String interestFreeInstall) {
        return Card.builder()
                .bin(bin)
                .cardType(cardType)
                .installMonth(installMonth)
                .interestFreeInstall(interestFreeInstall)
                .build();
    }

}

