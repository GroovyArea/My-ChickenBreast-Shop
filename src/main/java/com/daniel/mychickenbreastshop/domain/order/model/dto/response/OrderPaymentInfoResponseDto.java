package com.daniel.mychickenbreastshop.domain.order.model.dto.response;

import com.daniel.mychickenbreastshop.domain.order.model.model.OrderStatus;
import com.daniel.mychickenbreastshop.domain.payment.model.enums.PayStatus;
import com.daniel.mychickenbreastshop.domain.payment.model.enums.PaymentType;
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
