package com.daniel.mychickenbreastshop.domain.product.model.item.dto.request;

import com.daniel.mychickenbreastshop.domain.product.model.category.model.ChickenCategory;
import com.daniel.mychickenbreastshop.domain.product.model.item.model.ChickenStatus;
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

    private String image;

    private ChickenStatus status;

    @NotBlank(message = "필수 입력 값입니다.")
    private ChickenCategory category;

    public void updateImageName(String imageName) {
        image = imageName;
    }
}