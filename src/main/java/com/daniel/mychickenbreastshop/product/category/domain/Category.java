package com.daniel.mychickenbreastshop.product.category.domain;

import com.daniel.mychickenbreastshop.global.domain.BaseTimeEntity;
import com.daniel.mychickenbreastshop.product.category.domain.enums.ChickenCategory;
import com.daniel.mychickenbreastshop.product.item.domain.Product;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Category extends BaseTimeEntity<Category> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "category_name")
    private ChickenCategory categoryName;

    /* <비즈니스 로직 메서드> */

    public void updateCategoryName(ChickenCategory newCategoryName) {
        this.categoryName = newCategoryName;
    }
}
