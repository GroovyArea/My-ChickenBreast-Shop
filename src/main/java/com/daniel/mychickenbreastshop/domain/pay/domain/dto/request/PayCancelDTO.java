package com.daniel.mychickenbreastshop.domain.pay.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayCancelDTO {

    String tid;

    Integer cancelAmount;

    Integer cancelTaxFreeAmount;
}
