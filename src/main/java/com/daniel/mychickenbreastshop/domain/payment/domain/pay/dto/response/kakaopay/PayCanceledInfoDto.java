package com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.response.kakaopay;

import com.daniel.mychickenbreastshop.domain.payment.domain.order.dto.response.kakaopay.ApprovedCancelAmount;
import com.daniel.mychickenbreastshop.domain.payment.domain.order.dto.response.kakaopay.CancelAvailableAmount;
import com.daniel.mychickenbreastshop.domain.payment.domain.order.dto.response.kakaopay.CanceledAmount;
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
public class PayCanceledInfoDto {

    private String cid;
    private String tid;
    private String aid;
    private String status;
    private String partnerOrderId;
    private String partnerUserId;
    private String paymentMethodType;
    private Amount amount;
    private ApprovedCancelAmount approvedCancelAmount;
    private CanceledAmount canceledAmount;
    private CancelAvailableAmount cancelAvailableAmount;
    private String itemName;
    private String itemCode;
    private Integer quantity;
    private Date createdAt;
    private Date approvedAt;
    private Date canceledAt;
}
