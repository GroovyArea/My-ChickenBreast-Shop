package com.daniel.mychickenbreastshop.domain.payment.domain.pay;

import com.daniel.mychickenbreastshop.global.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "CARD_INFO")
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
}

