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
    private Long itemNo;

    @NotBlank(message = "필수 입력 값입니다.")
    private String itemName;

    @NotNull(message = "필수 입력 값입니다.")
    private Integer itemQuantity;

    @NotNull(message = "필수 입력 값입니다.")
    private Long totalPrice;

}
