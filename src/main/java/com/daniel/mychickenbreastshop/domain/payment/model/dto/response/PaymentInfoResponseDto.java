package com.daniel.mychickenbreastshop.domain.payment.model.dto.response;

import com.daniel.mychickenbreastshop.domain.payment.model.model.PayStatus;
import com.daniel.mychickenbreastshop.domain.payment.model.model.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentInfoResponseDto {

    private Long paymentId;
    private Integer totalPrice;
    private PaymentType paymentType;
    private PayStatus payStatus;
    private Long cardId;
    private String cardBin;
    private String cardType;
    private String installMonth;
    private String interestFreeInstall;

}
