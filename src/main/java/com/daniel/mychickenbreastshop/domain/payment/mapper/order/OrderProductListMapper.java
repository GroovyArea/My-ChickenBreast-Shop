package com.daniel.mychickenbreastshop.domain.payment.mapper.order;

import com.daniel.mychickenbreastshop.domain.payment.domain.order.OrderProduct;
import com.daniel.mychickenbreastshop.domain.payment.domain.order.dto.response.OrderProductResponseDto;
import com.daniel.mychickenbreastshop.global.mapper.GenericDtoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderProductListMapper extends GenericDtoMapper<OrderProductResponseDto, OrderProduct> {

    @Override
    @Mapping(target = "orderProductId", source = "id")
    OrderProductResponseDto toDTO(OrderProduct orderProduct);
}
