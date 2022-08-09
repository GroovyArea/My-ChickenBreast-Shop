package com.daniel.mychickenbreastshop.domain.order.domain;

import com.daniel.mychickenbreastshop.global.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderedProduct extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private int count;

    @Column(name = "product_name")
    private String name;

    @Column(name = "product_price")
    private String price;

    @Column(name = "product_image")
    private String image;

    @Column(name = "product_content")
    private String content;

}
