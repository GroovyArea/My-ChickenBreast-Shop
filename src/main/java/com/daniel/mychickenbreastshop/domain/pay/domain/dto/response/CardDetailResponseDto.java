package com.daniel.mychickenbreastshop.domain.pay.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardDetailResponseDto {

    private Long id;
    private String bin;
    private String cardType;
    private String installMonth;
    private String interestFreeInstall;
}
