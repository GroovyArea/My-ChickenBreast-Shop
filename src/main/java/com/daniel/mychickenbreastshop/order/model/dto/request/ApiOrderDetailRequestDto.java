package com.daniel.mychickenbreastshop.order.model.dto.request;

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
