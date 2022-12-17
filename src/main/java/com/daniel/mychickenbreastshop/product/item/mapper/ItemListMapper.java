package com.daniel.mychickenbreastshop.product.item.mapper;


import com.daniel.mychickenbreastshop.global.mapper.GenericDtoMapper;
import com.daniel.mychickenbreastshop.product.category.domain.Category;
import com.daniel.mychickenbreastshop.product.category.domain.enums.ChickenCategory;
import com.daniel.mychickenbreastshop.product.item.domain.Product;
import com.daniel.mychickenbreastshop.product.item.model.dto.response.ListResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemListMapper extends GenericDtoMapper<ListResponseDto, Product> {

    default ChickenCategory getCategory(Category category){
        return category.getCategoryName();
    }
}
