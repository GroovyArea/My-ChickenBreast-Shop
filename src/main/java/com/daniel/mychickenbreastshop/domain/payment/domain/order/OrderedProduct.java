package com.daniel.mychickenbreastshop.domain.payment.domain.order;

import com.daniel.mychickenbreastshop.global.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "ORDER_PRODUCT")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderedProduct extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer count;

    @Column(name = "product_name", nullable = false)
    private String name;

    @Column(name = "product_price", nullable = false)
    private Integer price;

    @Column(name = "product_image", nullable = false)
    private String image;

    @Column(name = "product_content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;
}