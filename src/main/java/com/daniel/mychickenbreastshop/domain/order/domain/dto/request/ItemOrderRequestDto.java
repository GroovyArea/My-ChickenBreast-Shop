package com.daniel.mychickenbreastshop.domain.order.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemOrderRequestDto {

    @NotNull(message = "수량을 필히 입력하세요.")
    private Integer quantity;

    @NotNull(message = "상품 번호를 필히 입력하세요.")
    private Integer itemNumber;

    @NotBlank(message = "상품 이름을 필히 입력하세요.")
    private String itemName;

    @NotNull(message = "총 가격을 필히 입력하세요.")
    private Integer totalAmount;
}
