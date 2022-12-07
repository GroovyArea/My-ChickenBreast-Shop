package com.daniel.ddd.order.mapper;

import com.daniel.ddd.global.mapper.GenericDtoMapper;
import com.daniel.ddd.order.domain.Order;
import com.daniel.ddd.order.model.dto.response.OrderInfoListResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderInfoListMapper extends GenericDtoMapper<OrderInfoListResponseDto, Order> {

    @Override
    @Mapping(target = "orderId", source = "id")
    OrderInfoListResponseDto toDTO(Order order);
}
