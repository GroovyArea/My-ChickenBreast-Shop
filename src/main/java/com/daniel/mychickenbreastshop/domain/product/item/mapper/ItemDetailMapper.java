package com.daniel.mychickenbreastshop.domain.product.item.mapper;

import com.daniel.mychickenbreastshop.domain.product.item.model.Product;
import com.daniel.mychickenbreastshop.domain.product.item.model.dto.response.DetailResponseDto;
import com.daniel.mychickenbreastshop.global.mapper.GenericDtoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemDetailMapper extends GenericDtoMapper<DetailResponseDto, Product> {

    @Mapping(target = "categoryName", source = "product.category.categoryName")
    @Override
    DetailResponseDto toDTO(Product product);
}
