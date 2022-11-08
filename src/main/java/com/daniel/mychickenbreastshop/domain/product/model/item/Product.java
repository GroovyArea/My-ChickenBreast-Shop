package com.daniel.mychickenbreastshop.domain.product.model.item;

import com.daniel.mychickenbreastshop.domain.product.model.category.Category;
import com.daniel.mychickenbreastshop.domain.product.model.item.enums.ChickenStatus;
import com.daniel.mychickenbreastshop.global.domain.BaseTimeEntity;
import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.daniel.mychickenbreastshop.domain.product.model.item.enums.ProductResponse.ITEM_QUANTITY_NOT_ENOUGH;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private Integer price;

    private Integer quantity;

    private String content;

    private String image;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_status")
    private ChickenStatus status;

    // <비즈니스 로직 메서드> //

    public void checkStockQuantity(int requestQuantity) {
        if(this.quantity < requestQuantity) {
            throw new BadRequestException(ITEM_QUANTITY_NOT_ENOUGH.getMessage());
        }
    }

    public void decreaseItemQuantity(int quantity) {
        if (this.quantity - quantity < 0) {
            throw new BadRequestException(ITEM_QUANTITY_NOT_ENOUGH.getMessage());
        }
        this.quantity -= quantity;
    }

    public void increaseItemQuantity(int quantity) {
        this.quantity += quantity;
    }

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

