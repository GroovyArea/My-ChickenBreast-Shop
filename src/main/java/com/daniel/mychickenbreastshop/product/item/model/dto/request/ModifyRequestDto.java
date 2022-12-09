package com.daniel.mychickenbreastshop.product.item.model.dto.request;


import com.daniel.mychickenbreastshop.product.category.domain.enums.ChickenCategory;
import com.daniel.mychickenbreastshop.product.item.domain.enums.ChickenStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ModifyRequestDto {

    @NotNull(message = "필수 입력 값입니다.")
    private Long id;

    @NotBlank(message = "필수 입력 값입니다.")
    private String name;

    @NotNull(message = "필수 입력 값입니다.")
    private Integer price;

    @NotNull(message = "필수 입력 값입니다.")
    private Integer quantity;

    @NotBlank(message = "필수 입력 값입니다.")
    private String content;

    private String image;

    @NotNull(message = "필수 입력 값입니다.")
    private ChickenStatus status;

    @NotNull(message = "필수 입력 값입니다.")
    private ChickenCategory category;
}
