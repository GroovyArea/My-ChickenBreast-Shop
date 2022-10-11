package com.daniel.mychickenbreastshop.domain.product.mapper;

import com.daniel.mychickenbreastshop.domain.product.model.item.Product;
import com.daniel.mychickenbreastshop.domain.product.model.item.dto.request.RegisterRequestDto;
import com.daniel.mychickenbreastshop.global.mapper.GenericEntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemRegisterMapper extends GenericEntityMapper<RegisterRequestDto, Product> {
    @Override
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "id", ignore = true)
    Product toEntity(RegisterRequestDto registerRequestDto);
}
