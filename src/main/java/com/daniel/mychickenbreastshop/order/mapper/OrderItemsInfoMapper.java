package com.daniel.mychickenbreastshop.order.mapper;

import com.daniel.mychickenbreastshop.global.mapper.GenericDtoMapper;
import com.daniel.mychickenbreastshop.order.domain.Order;
import com.daniel.mychickenbreastshop.order.model.dto.response.OrderItemsInfoResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemsInfoMapper extends GenericDtoMapper<OrderItemsInfoResponseDto, Order> {

    @Override
    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "orderInfoResponseDtos", ignore = true)
    OrderItemsInfoResponseDto toDTO(Order order);

}
