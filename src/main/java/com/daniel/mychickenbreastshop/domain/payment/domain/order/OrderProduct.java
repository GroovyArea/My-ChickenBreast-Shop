package com.daniel.mychickenbreastshop.domain.payment.domain.order;

import com.daniel.mychickenbreastshop.global.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "ORDER_PRODUCT")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderProduct extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer count;

    @Column(name = "product_name")
    private String name;

    @Column(name = "product_price")
    private Integer price;

    @Column(name = "product_image")
    private String image;

    @Column(name = "product_content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // <연관관계 편의 메서드> //
    public void updateOrderInfo(Order orderInfo) {
        order = orderInfo;
    }

    // <주문 상품 생성 메서드> //
    public static OrderProduct createOrderProduct(int count, String name, int price, String image, String content) {
        return OrderProduct.builder()
                .count(count)
                .name(name)
                .price(price)
                .image(image)
                .content(content)
                .build();
    }

}
