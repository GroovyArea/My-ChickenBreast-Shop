package com.daniel.ddd.order.mapper;

import com.daniel.ddd.order.domain.OrderProduct;
import com.daniel.ddd.order.model.dto.response.OrderProductResponseDto;
import com.daniel.mychickenbreastshop.global.mapper.GenericDtoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderProductListMapper extends GenericDtoMapper<OrderProductResponseDto, OrderProduct> {

    @Override
    @Mapping(target = "orderProductId", source = "id")
    OrderProductResponseDto toDTO(OrderProduct orderProduct);
}
