package com.daniel.mychickenbreastshop.domain.order.domain.dto.response;

import com.daniel.mychickenbreastshop.domain.order.domain.model.OrderStatus;
import com.daniel.mychickenbreastshop.domain.pay.domain.dto.response.CardDetailResponseDto;
import com.daniel.mychickenbreastshop.domain.pay.domain.dto.response.PaymentDetailResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class OrderDetailResponseDto {

    private Long id;
    private Integer orderPrice;
    private Integer totalCount;
    private OrderStatus status;
    private OrderedProductResponseDto orderedProductResponseDto;
    private PaymentDetailResponseDto paymentDetailResponseDto;
    private CardDetailResponseDto cardDetailResponseDto;
}
