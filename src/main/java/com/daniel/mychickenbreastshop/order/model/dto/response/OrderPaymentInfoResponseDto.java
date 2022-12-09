package com.daniel.mychickenbreastshop.order.model.dto.response;

import com.daniel.mychickenbreastshop.order.domain.enums.OrderStatus;
import com.daniel.mychickenbreastshop.payment.domain.enums.PayStatus;
import com.daniel.mychickenbreastshop.payment.domain.enums.PaymentType;
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
    private OrderStatus orderStatus;
    private Long paymentId;
    private Long totalPrice;
    private PaymentType paymentType;
    private PayStatus payStatus;

}
