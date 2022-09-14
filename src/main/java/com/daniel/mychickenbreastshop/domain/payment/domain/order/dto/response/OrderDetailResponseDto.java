package com.daniel.mychickenbreastshop.domain.payment.domain.order.dto.response;

import com.daniel.mychickenbreastshop.domain.payment.domain.order.model.OrderStatus;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.response.CardDetailResponseDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.response.PaymentDetailResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 주문 세부 정보
 */
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
