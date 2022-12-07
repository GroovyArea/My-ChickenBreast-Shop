package com.daniel.ddd.product.item.mapper;

import com.daniel.mychickenbreastshop.domain.product.item.model.Product;
import com.daniel.mychickenbreastshop.domain.product.item.model.dto.request.ModifyRequestDto;
import com.daniel.mychickenbreastshop.global.mapper.GenericEntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemModifyMapper extends GenericEntityMapper<ModifyRequestDto, Product> {
    @Override
    @Mapping(target = "category", ignore = true)
    Product toEntity(ModifyRequestDto modifyRequestDto);
}