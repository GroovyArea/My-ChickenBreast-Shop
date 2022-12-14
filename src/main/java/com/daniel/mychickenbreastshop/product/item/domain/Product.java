package com.daniel.mychickenbreastshop.product.item.domain;

import com.daniel.mychickenbreastshop.global.domain.BaseTimeEntity;
import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import com.daniel.mychickenbreastshop.product.category.domain.Category;
import com.daniel.mychickenbreastshop.product.item.domain.enums.ChickenStatus;
import lombok.*;

import javax.persistence.*;

import static com.daniel.mychickenbreastshop.product.item.domain.enums.ErrorMessages.ITEM_QUANTITY_NOT_ENOUGH;

@Table(indexes = {
        @Index(name = "idx__name__status", columnList = "name, product_status", unique = true)
})
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseTimeEntity<Product> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
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
        if (this.quantity < requestQuantity) {
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
        this.name = updatableEntity.getName();
        this.price = updatableEntity.getPrice();
        this.quantity = updatableEntity.getQuantity();
        this.content = updatableEntity.getContent();
        updateImageInfo(updatableEntity.getImage());
        this.status = updatableEntity.getStatus();
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

    public void updateImageInfo(final String uploadFileName) {
        this.image = uploadFileName;
    }

    public void remove() {
        this.status = ChickenStatus.EXTINCTION;
        this.delete();
    }
}

