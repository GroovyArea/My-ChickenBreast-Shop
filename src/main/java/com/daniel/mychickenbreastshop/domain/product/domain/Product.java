package com.daniel.mychickenbreastshop.domain.product.domain;

import com.daniel.mychickenbreastshop.domain.product.enums.ChickenStatus;
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
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private int price;

    private int quantity;

    private String content;

    private String image;

    @Enumerated(EnumType.STRING)
    private ChickenStatus status;
}
