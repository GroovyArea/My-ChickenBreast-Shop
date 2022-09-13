package com.daniel.mychickenbreastshop.domain.payment.domain.order.dto.response.kakaopay;

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
public class SelectedCardInfo {

    private String cardBin;
    private String cardCorpName;
    private String interestFreeInstall;
    private Integer installMonth;
}
