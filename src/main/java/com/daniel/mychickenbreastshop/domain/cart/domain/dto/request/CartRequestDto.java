package com.daniel.mychickenbreastshop.domain.cart.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartRequestDto {

    @NotNull(message = "필수 입력 값입니다.")
    private Long productNo;

    @NotBlank(message = "필수 입력 값입니다.")
    private String productName;

    @NotNull(message = "필수 입력 값입니다.")
    private int productQuantity;

    @NotNull(message = "필수 입력 값입니다.")
    private long productPrice;

}
