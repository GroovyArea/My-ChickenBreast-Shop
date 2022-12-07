package com.daniel.ddd.order.mapper;

import com.daniel.ddd.global.mapper.GenericDtoMapper;
import com.daniel.ddd.global.mapper.GenericEntityMapper;
import com.daniel.ddd.order.domain.OrderProduct;
import com.daniel.ddd.order.model.dto.request.OrderRequestDto;
import com.daniel.ddd.order.model.dto.response.OrderProductResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderProductsMapper extends GenericDtoMapper<OrderProductResponseDto, OrderProduct>,
        GenericEntityMapper<OrderRequestDto, OrderProduct> {

    @Override
    @Mapping(target = "orderProductId", source = "id")
    OrderProductResponseDto toDTO(OrderProduct orderProduct);

    @Override
    @Mapping(target = "count", source = "quantity")
    @Mapping(target = "name", source = "itemName")
    @Mapping(target = "image", source = "itemImageUrl")
    OrderProduct toEntity(OrderRequestDto orderRequestDto);

}
