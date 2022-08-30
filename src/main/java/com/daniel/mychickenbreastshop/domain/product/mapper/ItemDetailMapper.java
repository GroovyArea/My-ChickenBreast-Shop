package com.daniel.mychickenbreastshop.domain.product.mapper;

import com.daniel.mychickenbreastshop.domain.product.domain.item.Product;
import com.daniel.mychickenbreastshop.domain.product.domain.item.dto.response.DetailResponseDto;
import com.daniel.mychickenbreastshop.global.mapper.GenericDtoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemDetailMapper extends GenericDtoMapper<DetailResponseDto, Product> {

    @Mapping(target = "categoryName", source = "product.category.categoryName.chickenName")
    @Override
    DetailResponseDto toDTO(Product product);
}
