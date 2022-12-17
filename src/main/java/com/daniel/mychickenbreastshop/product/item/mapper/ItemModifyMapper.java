package com.daniel.mychickenbreastshop.product.item.mapper;

import com.daniel.mychickenbreastshop.global.mapper.GenericEntityMapper;
import com.daniel.mychickenbreastshop.product.item.domain.Product;
import com.daniel.mychickenbreastshop.product.item.model.dto.request.ModifyRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemModifyMapper extends GenericEntityMapper<ModifyRequestDto, Product> {
    @Override
    @Mapping(target = "category", ignore = true)
    Product toEntity(ModifyRequestDto modifyRequestDto);
}
