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
public class PayCancelRequestDto {

    @NotBlank(message = "결제 고유 번호를 입력하세요.")
    String payId;

    @NotNull(message = "취소 금액을 입력하세요.")
    Integer cancelAmount;

    @NotNull(message = "취소 비과세 금액을 입력하세요.")
    Integer cancelTaxFreeAmount;
}
