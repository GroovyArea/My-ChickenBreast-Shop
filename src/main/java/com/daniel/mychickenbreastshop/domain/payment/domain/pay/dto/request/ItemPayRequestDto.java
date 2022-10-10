package com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.request;

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
public class ItemPayRequestDto {

    @NotNull(message = "상품 번호를 입력하세요.")
    private Long itemNumber;

    @NotNull(message = "수량을 입력하세요.")
    private Integer quantity;

    @NotBlank(message = "상품 이름을 입력하세요.")
    private String itemName;

    @NotNull(message = "총 가격을 입력하세요.")
    private Integer totalAmount;
}
