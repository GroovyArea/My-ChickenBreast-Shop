package com.daniel.ddd.product.item.mapper;


import com.daniel.ddd.global.mapper.GenericDtoMapper;
import com.daniel.ddd.product.item.domain.Product;
import com.daniel.ddd.user.model.dto.response.DetailResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemDetailMapper extends GenericDtoMapper<DetailResponseDto, Product> {

    @Mapping(target = "categoryName", source = "product.category.categoryName")
    @Override
    DetailResponseDto toDTO(Product product);
}
