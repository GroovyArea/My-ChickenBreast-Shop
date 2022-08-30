package com.daniel.mychickenbreastshop.domain.product.mapper;

import com.daniel.mychickenbreastshop.domain.product.domain.item.Product;
import com.daniel.mychickenbreastshop.domain.product.domain.item.dto.request.ModifyRequestDto;
import com.daniel.mychickenbreastshop.global.mapper.GenericEntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemModifyMapper extends GenericEntityMapper<ModifyRequestDto, Product> {
    @Override
    @Mapping(target = "category", ignore = true)
    Product toEntity(ModifyRequestDto modifyRequestDto);
}
