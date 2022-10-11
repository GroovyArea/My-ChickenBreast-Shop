package com.daniel.mychickenbreastshop.domain.order.mapper;

import com.daniel.mychickenbreastshop.domain.payment.model.order.Order;
import com.daniel.mychickenbreastshop.domain.payment.model.order.dto.response.OrderInfoListResponseDto;
import com.daniel.mychickenbreastshop.global.mapper.GenericDtoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderInfoListMapper extends GenericDtoMapper<OrderInfoListResponseDto, Order> {

    @Override
    @Mapping(target = "orderId", source = "id")
    OrderInfoListResponseDto toDTO(Order order);
}
