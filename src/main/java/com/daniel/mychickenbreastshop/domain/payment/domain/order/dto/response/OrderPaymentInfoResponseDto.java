package com.daniel.mychickenbreastshop.domain.payment.domain.order.dto.response;

import com.daniel.mychickenbreastshop.domain.payment.domain.order.model.OrderStatus;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.model.PayStatus;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.model.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderPaymentInfoResponseDto {

    private Long orderId;
    private Integer totalCount;
    private OrderStatus status;
    private Long paymentId;
    private Integer totalPrice;
    private PaymentType paymentType;
    private PayStatus payStatus;

}
