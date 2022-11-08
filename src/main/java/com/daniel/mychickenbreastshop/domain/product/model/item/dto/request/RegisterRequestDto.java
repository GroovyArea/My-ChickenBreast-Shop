package com.daniel.mychickenbreastshop.domain.product.model.item.dto.request;

import com.daniel.mychickenbreastshop.domain.product.model.category.enums.ChickenCategory;
import com.daniel.mychickenbreastshop.domain.product.model.item.enums.ChickenStatus;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RegisterRequestDto {

    @NotBlank(message = "필수 입력 값입니다.")
    private String name;

    @NotBlank(message = "필수 입력 값입니다.")
    private Integer price;

    @NotNull(message = "필수 입력 값입니다.")
    private Integer quantity;

    @NotNull(message = "필수 입력 값입니다.")
    private String content;

    private String image;

    @NotNull(message = "필수 입력 값입니다.")
    private ChickenStatus status;

    @NotBlank(message = "필수 입력 값입니다.")
    private ChickenCategory category;

    public void updateImageName(String imageName) {
        image = imageName;
    }
}
