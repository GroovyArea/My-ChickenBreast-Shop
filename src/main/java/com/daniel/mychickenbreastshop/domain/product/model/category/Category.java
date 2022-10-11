package com.daniel.mychickenbreastshop.domain.product.model.category;

import com.daniel.mychickenbreastshop.domain.product.model.category.model.ChickenCategory;
import com.daniel.mychickenbreastshop.domain.product.model.item.Product;
import com.daniel.mychickenbreastshop.global.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category extends BaseTimeEntity {

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
