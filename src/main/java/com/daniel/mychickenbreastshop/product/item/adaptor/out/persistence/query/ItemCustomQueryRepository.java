package com.daniel.mychickenbreastshop.product.item.adaptor.out.persistence.query;


import com.daniel.mychickenbreastshop.product.category.domain.enums.ChickenCategory;
import com.daniel.mychickenbreastshop.product.item.domain.Product;
import com.daniel.mychickenbreastshop.product.item.model.dto.request.ItemSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemCustomQueryRepository {

    Page<Product> findItemWithDynamicQuery(Pageable pageable, ItemSearchDto itemSearchDto, ChickenCategory category);

    Page<Product> findAllByCategoryName(ChickenCategory categoryName, Pageable pageable);
}
