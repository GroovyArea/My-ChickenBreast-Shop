package com.daniel.mychickenbreastshop.domain.order.mapper;

import com.daniel.mychickenbreastshop.domain.order.model.Order;
import com.daniel.mychickenbreastshop.domain.order.model.dto.response.OrderItemsInfoResponseDto;
import com.daniel.mychickenbreastshop.global.mapper.GenericDtoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemsInfoMapper extends GenericDtoMapper<OrderItemsInfoResponseDto, Order> {

    @Override
    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "orderInfoResponseDtos", ignore = true)
    OrderItemsInfoResponseDto toDTO(Order order);

}
