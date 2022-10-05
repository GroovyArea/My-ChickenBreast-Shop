package com.daniel.mychickenbreastshop.domain.payment.mapper.payment;

import com.daniel.mychickenbreastshop.domain.payment.domain.pay.Payment;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.response.PaymentInfoResponseDto;
import com.daniel.mychickenbreastshop.global.mapper.GenericDtoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentInfoMapper extends GenericDtoMapper<PaymentInfoResponseDto, Payment> {

    @Override
    @Mapping(target = "paymentId", source = "id")
    @Mapping(target = "cardId", source = "card.id")
    @Mapping(target = "cardBin", source = "card.bin")
    PaymentInfoResponseDto toDTO(Payment payment);
}
