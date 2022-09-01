package com.daniel.mychickenbreastshop.domain.product.domain.item;

import com.daniel.mychickenbreastshop.domain.product.domain.item.model.ChickenStatus;
import com.daniel.mychickenbreastshop.domain.product.domain.category.Category;
import com.daniel.mychickenbreastshop.global.domain.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @Column(nullable = false)
    private String name;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private Integer price;

    private Integer quantity;

    private String content;

    private String image;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_status", nullable = false)
    private ChickenStatus status;

    public void updateProductInfo(final Product updatableEntity) {
        updateName(updatableEntity.getName());
        updatePrice(updatableEntity.getPrice());
        updateQuantity(updatableEntity.getQuantity());
        updateContent(updatableEntity.getContent());
        updateItemStatus(updatableEntity.getStatus());
    }

    private void updateName(final String name) {
        this.name = name;
    }

    private void updatePrice(final Integer price) {
        this.price = price;
    }

    private void updateQuantity(final Integer quantity) {
        this.quantity = quantity;
    }

    private void updateContent(final String content) {
        this.content = content;
    }

    public void updateImageInfo(final String image) {
        this.image = image;
    }

    public void updateItemStatus(final ChickenStatus status) {
        this.status = status;
    }

    public void updateCategoryInfo(final Category updatableCategory) {
        if (this.category != null) {
            this.category.getProducts().remove(this);
        }

        this.category = updatableCategory;
        this.category.getProducts().add(this);
    }
}
