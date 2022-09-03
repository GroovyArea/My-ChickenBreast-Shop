package com.daniel.mychickenbreastshop.domain.order.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderVO {

    private String tid;
    private String userId;
    private String aid;
    private String cid;
    private String partnerOrderId;
    private String partnerUserId;
    private String paymentMethodType;
    private String itemName;
    private String itemCode;
    private Integer quantity;
    private String createdAt;
    private String approvedAt;
    private CardVO card;
    private AmountVO amount;
    private String orderStatus;
}
