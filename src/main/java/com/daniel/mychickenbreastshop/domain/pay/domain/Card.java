package com.daniel.mychickenbreastshop.domain.pay.domain;

import com.daniel.mychickenbreastshop.global.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "cardinfo")
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
}

