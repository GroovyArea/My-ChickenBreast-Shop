package com.daniel.mychickenbreastshop.domain.pay.domain.dto.response;

import com.daniel.mychickenbreastshop.domain.pay.domain.model.PayStatus;
import com.daniel.mychickenbreastshop.domain.pay.domain.model.PaymentType;
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
