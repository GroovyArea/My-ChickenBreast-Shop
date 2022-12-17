package com.daniel.mychickenbreastshop.order.mapper;

import com.daniel.mychickenbreastshop.global.mapper.GenericDtoMapper;
import com.daniel.mychickenbreastshop.global.mapper.GenericEntityMapper;
import com.daniel.mychickenbreastshop.order.domain.OrderProduct;
import com.daniel.mychickenbreastshop.order.model.dto.request.OrderRequestDto;
import com.daniel.mychickenbreastshop.order.model.dto.response.OrderProductResponseDto;
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
