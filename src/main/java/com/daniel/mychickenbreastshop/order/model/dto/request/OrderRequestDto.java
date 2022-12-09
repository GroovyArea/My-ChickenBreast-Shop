package com.daniel.mychickenbreastshop.order.model.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class OrderRequestDto {

    @NotNull(message = "상품 번호를 입력하세요.")
    private Long itemNumber;

    @NotBlank(message = "상품 이름을 입력하세요.")
    private String itemName;

    @NotBlank(message = "상품 이미지 url 을 입력하세요.")
    private String itemImageUrl;

    @NotNull(message = "수량을 입력하세요.")
    private Integer quantity;

    @NotNull(message = "총 가격을 입력하세요.")
    private Integer totalAmount;
}
