package com.daniel.mychickenbreastshop.payment.domain;

import com.daniel.mychickenbreastshop.global.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Card extends BaseTimeEntity<Card> {

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

    // <카드 생성 메서드> //
    public static Card createCard(final String bin,
                                  final String cardType,
                                  final String installMonth,
                                  final String interestFreeInstall) {
        return Card.builder()
                .bin(bin)
                .cardType(cardType)
                .installMonth(installMonth)
                .interestFreeInstall(interestFreeInstall)
                .build();
    }

}

