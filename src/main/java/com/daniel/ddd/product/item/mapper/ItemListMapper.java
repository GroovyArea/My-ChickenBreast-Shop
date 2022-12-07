package com.daniel.ddd.product.item.mapper;

import com.daniel.mychickenbreastshop.domain.product.category.model.Category;
import com.daniel.mychickenbreastshop.domain.product.category.model.enums.ChickenCategory;
import com.daniel.mychickenbreastshop.domain.product.item.model.Product;
import com.daniel.mychickenbreastshop.domain.product.item.model.dto.response.ListResponseDto;
import com.daniel.mychickenbreastshop.global.mapper.GenericDtoMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemListMapper extends GenericDtoMapper<ListResponseDto, Product> {

    default ChickenCategory getCategory(Category category){
        return category.getCategoryName();
    }
}
