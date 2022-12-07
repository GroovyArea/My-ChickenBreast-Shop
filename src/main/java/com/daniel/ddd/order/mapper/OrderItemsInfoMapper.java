package com.daniel.ddd.order.mapper;

import com.daniel.ddd.global.mapper.GenericDtoMapper;
import com.daniel.ddd.order.domain.Order;
import com.daniel.ddd.order.model.dto.response.OrderItemsInfoResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemsInfoMapper extends GenericDtoMapper<OrderItemsInfoResponseDto, Order> {

    @Override
    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "orderInfoResponseDtos", ignore = true)
    OrderItemsInfoResponseDto toDTO(Order order);

}
