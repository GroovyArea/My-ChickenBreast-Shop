package com.daniel.mychickenbreastshop.domain.order.domain.dto.response.kakaopay;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public class ApprovedCancelAmount {

    private Integer total;
    private Integer taxFree;
    private Integer vat;
    private Integer point;
    private Integer discount;
}
