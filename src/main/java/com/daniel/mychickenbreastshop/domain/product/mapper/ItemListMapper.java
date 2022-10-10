package com.daniel.mychickenbreastshop.domain.product.mapper;

import com.daniel.mychickenbreastshop.domain.product.domain.category.Category;
import com.daniel.mychickenbreastshop.domain.product.domain.category.model.ChickenCategory;
import com.daniel.mychickenbreastshop.domain.product.domain.item.Product;
import com.daniel.mychickenbreastshop.domain.product.domain.item.dto.response.ListResponseDto;
import com.daniel.mychickenbreastshop.global.mapper.GenericDtoMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemListMapper extends GenericDtoMapper<ListResponseDto, Product> {

    default ChickenCategory getCategory(Category category){
        return category.getCategoryName();
    }
}
