package com.daniel.mychickenbreastshop.payment.mapper;

import com.daniel.mychickenbreastshop.payment.domain.Payment;
import com.daniel.mychickenbreastshop.payment.model.dto.response.PaymentInfoResponseDto;
import com.daniel.mychickenbreastshop.global.mapper.GenericDtoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentInfoMapper extends GenericDtoMapper<PaymentInfoResponseDto, Payment> {

    @Override
    @Mapping(target = "getPaymentId", source = "payment.id")
    @Mapping(target = "cardId", source = "card.id")
    @Mapping(target = "cardBin", source = "card.bin")
    PaymentInfoResponseDto toDTO(Payment payment);
}
