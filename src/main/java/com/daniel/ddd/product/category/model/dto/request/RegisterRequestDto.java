package com.daniel.ddd.product.category.model.dto.request;

import com.daniel.mychickenbreastshop.domain.product.category.model.enums.ChickenCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {

    @NotBlank(message = "필수 입력 값입니다.")
    private ChickenCategory categoryName;
}
