package com.daniel.mychickenbreastshop.domain.payment.domain.order.dto.response.kakaopay;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public class CancelAvailableAmount {

    Integer total;
    Integer taxFree;
    Integer vat;
    Integer point;
    Integer discount;
}
