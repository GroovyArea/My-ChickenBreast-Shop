package com.daniel.ddd.order.domain;

import com.daniel.ddd.global.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Table(name = "ORDER_PRODUCT")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderProduct extends BaseTimeEntity<OrderProduct> {

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
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Order order;

    // <연관관계 편의 메서드> //
    public void updateOrderInfo(final Order orderInfo) {
        if (this.order != null) {
            order.getOrderProducts().remove(this);
        }

        this.order = orderInfo;
        order.getOrderProducts().add(this);
    }

    // <주문 상품 생성 메서드> //
    public static OrderProduct createOrderProduct(int count,
                                                  String name,
                                                  int price,
                                                  String image,
                                                  String content) {
        return OrderProduct.builder()
                .count(count)
                .name(name)
                .price(price)
                .image(image)
                .content(content)
                .build();
    }

}
