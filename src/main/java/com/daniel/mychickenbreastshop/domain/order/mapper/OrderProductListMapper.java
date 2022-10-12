package com.daniel.mychickenbreastshop.domain.order.mapper;

import com.daniel.mychickenbreastshop.domain.order.model.OrderProduct;
import com.daniel.mychickenbreastshop.domain.order.model.dto.response.OrderProductResponseDto;
import com.daniel.mychickenbreastshop.global.mapper.GenericDtoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderProductListMapper extends GenericDtoMapper<OrderProductResponseDto, OrderProduct> {

    @Override
    @Mapping(target = "orderProductId", source = "id")
    OrderProductResponseDto toDTO(OrderProduct orderProduct);
}
