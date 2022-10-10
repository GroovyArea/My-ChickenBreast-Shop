package com.daniel.mychickenbreastshop.domain.payment.mapper.order;

import com.daniel.mychickenbreastshop.domain.payment.domain.order.Order;
import com.daniel.mychickenbreastshop.domain.payment.domain.order.dto.response.OrderPaymentInfoResponseDto;
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
