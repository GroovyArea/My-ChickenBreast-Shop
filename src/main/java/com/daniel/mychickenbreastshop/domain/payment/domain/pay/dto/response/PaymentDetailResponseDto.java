package com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.response;

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
public class PaymentDetailResponseDto {

    private Long id;
    private String pgKey;
    private Integer totalPrice;
    private PaymentType paymentType;
    private PayStatus payStatus;

}
