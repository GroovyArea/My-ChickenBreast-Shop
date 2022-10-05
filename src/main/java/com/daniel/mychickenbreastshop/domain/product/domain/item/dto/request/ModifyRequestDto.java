package com.daniel.mychickenbreastshop.domain.product.domain.item.dto.request;

import com.daniel.mychickenbreastshop.domain.product.domain.category.model.ChickenCategory;
import com.daniel.mychickenbreastshop.domain.product.domain.item.model.ChickenStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ModifyRequestDto {

    @NotBlank(message = "필수 입력 값입니다.")
    private Long id;

    @NotBlank(message = "필수 입력 값입니다.")
    private String name;

    @NotBlank(message = "필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "필수 입력 값입니다.")
    private Integer quantity;

    @NotBlank(message = "필수 입력 값입니다.")
    private String content;

    private String image;

    @NotBlank(message = "필수 입력 값입니다.")
    private ChickenStatus status;

    @NotBlank(message = "필수 입력 값입니다.")
    private ChickenCategory category;
}
