package com.daniel.mychickenbreastshop.domain.payment.model.dto.response;

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
public class PaymentInfoResponseDto {

    private long paymentId;
    private long totalPrice;
    private PaymentType paymentType;
    private PayStatus payStatus;
    private long cardId;
    private String cardBin;
    private String cardType;
    private String installMonth;
    private String interestFreeInstall;

}
