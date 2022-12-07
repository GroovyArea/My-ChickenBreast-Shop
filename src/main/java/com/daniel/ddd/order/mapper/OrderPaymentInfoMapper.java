package com.daniel.ddd.order.mapper;

import com.daniel.ddd.order.domain.Order;
import com.daniel.ddd.order.model.dto.response.OrderPaymentInfoResponseDto;
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
