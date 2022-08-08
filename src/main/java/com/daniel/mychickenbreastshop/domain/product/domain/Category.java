package com.daniel.mychickenbreastshop.domain.product.domain;

import com.daniel.mychickenbreastshop.global.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    private String name;

    private LocalDateTime deletedAt;

}
