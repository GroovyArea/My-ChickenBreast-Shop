package com.daniel.mychickenbreastshop.domain.order.dto.response.kakaopay;

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
public class CardInfo {

    private String purchaseCorp;
    private String purchaseCorpCode;
    private String issuerCorp;
    private String issuerCorpCode;
    private String bin;
    private String cardType;
    private String installMonth;
    private String approvedId;
    private String cardMid;
    private String interestFreeInstall;
    private String cardItemCode;
}
