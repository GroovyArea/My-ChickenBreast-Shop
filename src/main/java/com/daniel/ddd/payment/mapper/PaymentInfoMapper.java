package com.daniel.ddd.payment.mapper;

import com.daniel.ddd.payment.domain.Payment;
import com.daniel.ddd.payment.model.dto.response.PaymentInfoResponseDto;
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
