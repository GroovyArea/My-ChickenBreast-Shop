package com.daniel.mychickenbreastshop.domain.product.mapper;

import com.daniel.mychickenbreastshop.domain.product.domain.item.Product;
import com.daniel.mychickenbreastshop.domain.product.domain.item.dto.request.RegisterRequestDto;
import com.daniel.mychickenbreastshop.global.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemRegisterMapper extends GenericMapper<RegisterRequestDto, Product> {
    @Override
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "id", ignore = true)
    Product toEntity(RegisterRequestDto registerRequestDto);
}
