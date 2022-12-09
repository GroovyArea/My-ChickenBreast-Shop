package com.daniel.mychickenbreastshop.order.mapper;

import com.daniel.mychickenbreastshop.global.mapper.GenericDtoMapper;
import com.daniel.mychickenbreastshop.order.domain.Order;
import com.daniel.mychickenbreastshop.order.model.dto.response.OrderPaymentInfoResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderPaymentInfoMapper extends GenericDtoMapper<OrderPaymentInfoResponseDto, Order> {

    @Override
    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "getPaymentId", source = "payment.id")
    OrderPaymentInfoResponseDto toDTO(Order order);
}
