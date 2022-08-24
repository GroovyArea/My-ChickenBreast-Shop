package com.daniel.mychickenbreastshop.domain.product.domain.category.dto;

import com.daniel.mychickenbreastshop.domain.product.domain.category.ChickenCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterRequestDto {

    private ChickenCategory name;
}
