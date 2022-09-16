package com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayCancelRequestDto {

    String payId;
    Integer cancelAmount;
    Integer cancelTaxFreeAmount;
}
