package com.daniel.mychickenbreastshop.product.category.model.dto.request;

import com.daniel.mychickenbreastshop.product.category.domain.enums.ChickenCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ModifyRequestDto {

    @NotBlank(message = "필수 입력 값입니다.")
    private ChickenCategory categoryName;
}
