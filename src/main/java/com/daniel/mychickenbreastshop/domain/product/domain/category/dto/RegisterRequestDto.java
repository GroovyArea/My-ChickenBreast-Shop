package com.daniel.mychickenbreastshop.domain.product.domain.category.dto;

import com.daniel.mychickenbreastshop.domain.product.domain.category.ChickenCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class RegisterRequestDto {

    @NotBlank(message = "필수 입력 값입니다.")
    private ChickenCategory categoryName;
}
