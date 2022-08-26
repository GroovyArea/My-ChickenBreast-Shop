package com.daniel.mychickenbreastshop.domain.product.domain.item.dto.request;

import com.daniel.mychickenbreastshop.domain.product.domain.category.ChickenCategory;
import com.daniel.mychickenbreastshop.domain.product.domain.item.model.ChickenStatus;
import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RegisterRequestDto {

    @NotBlank(message = "필수 입력 값입니다.")
    private String name;

    @NotBlank(message = "필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "필수 입력 값입니다.")
    private Integer quantity;

    @NotBlank(message = "필수 입력 값입니다.")
    private String content;

    @Setter
    private String image;

    @Setter
    private ChickenStatus status;

    @Setter
    private ChickenCategory category;
}
