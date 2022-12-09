package com.daniel.mychickenbreastshop.order.mapper;

import com.daniel.mychickenbreastshop.global.mapper.GenericDtoMapper;
import com.daniel.mychickenbreastshop.order.domain.Order;
import com.daniel.mychickenbreastshop.order.model.dto.response.OrderInfoListResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderInfoListMapper extends GenericDtoMapper<OrderInfoListResponseDto, Order> {

    @Override
    @Mapping(target = "orderId", source = "id")
    OrderInfoListResponseDto toDTO(Order order);
}
