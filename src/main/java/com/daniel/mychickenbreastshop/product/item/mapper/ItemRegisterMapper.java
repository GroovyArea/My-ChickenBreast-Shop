package com.daniel.mychickenbreastshop.product.item.mapper;

import com.daniel.mychickenbreastshop.global.mapper.GenericEntityMapper;
import com.daniel.mychickenbreastshop.product.item.domain.Product;
import com.daniel.mychickenbreastshop.product.item.model.dto.request.RegisterRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemRegisterMapper extends GenericEntityMapper<RegisterRequestDto, Product> {
    @Override
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "id", ignore = true)
    Product toEntity(RegisterRequestDto registerRequestDto);
}
