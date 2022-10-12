package com.daniel.mychickenbreastshop.domain.order.mapper;

import com.daniel.mychickenbreastshop.domain.order.model.Order;
import com.daniel.mychickenbreastshop.domain.order.model.dto.response.OrderPaymentInfoResponseDto;
import com.daniel.mychickenbreastshop.global.mapper.GenericDtoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderPaymentInfoMapper extends GenericDtoMapper<OrderPaymentInfoResponseDto, Order> {

    @Override
    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "paymentId", source = "payment.id")
    OrderPaymentInfoResponseDto toDTO(Order order);
}
