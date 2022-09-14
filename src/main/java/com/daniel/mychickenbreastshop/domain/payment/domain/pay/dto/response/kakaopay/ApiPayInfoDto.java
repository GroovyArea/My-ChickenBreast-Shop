package com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.response.kakaopay;

import com.daniel.mychickenbreastshop.domain.payment.domain.order.dto.response.kakaopay.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ApiPayInfoDto {

    String cid;
    String cidSecret;
    String tid;
    String status;
    String partnerOrderId;
    String partnerUserId;
    String paymentMethodType;
    Amount amount;
    CanceledAmount canceledAmount;
    CancelAvailableAmount cancelAvailableAmount;
    String itemName;
    String itemCode;
    Integer quantity;
    Date createdAt;
    Date approvedAt;
    Date canceledAt;
    SelectedCardInfo selectedCardInfo;
    PaymentActionDetails[] paymentActionDetails;
}
