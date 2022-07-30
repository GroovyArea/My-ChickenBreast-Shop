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
public class PaymentActionDetails {

    private String aid;
    private String approvedAt;
    private String paymentActionType;
    private String payload;
    private Integer amount;
    private Integer pointAmount;
    private Integer discountAmount;
}
