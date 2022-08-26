package com.daniel.mychickenbreastshop.domain.product.domain.category;

import com.daniel.mychickenbreastshop.domain.product.domain.item.Product;
import com.daniel.mychickenbreastshop.global.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ChickenCategory name;

    public void updateCategoryName(final ChickenCategory name) {
        this.name = name;
    }
}
