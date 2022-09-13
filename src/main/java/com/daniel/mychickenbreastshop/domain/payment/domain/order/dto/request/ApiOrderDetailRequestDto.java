package com.daniel.mychickenbreastshop.domain.payment.domain.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiOrderDetailRequestDto {

    String franchiseeId;
    String payId;
}
