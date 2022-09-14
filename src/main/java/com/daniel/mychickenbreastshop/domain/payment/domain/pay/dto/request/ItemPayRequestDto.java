package com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemPayRequestDto {

    private int quantity;
    private int itemNumber;
    private String itemName;
    private int totalAmount;
}
