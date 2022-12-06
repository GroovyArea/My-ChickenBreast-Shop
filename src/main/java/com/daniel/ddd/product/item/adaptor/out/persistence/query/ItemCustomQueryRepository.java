package com.daniel.ddd.product.item.adaptor.out.persistence.query;

import com.daniel.mychickenbreastshop.domain.product.category.model.enums.ChickenCategory;
import com.daniel.mychickenbreastshop.domain.product.item.model.Product;
import com.daniel.mychickenbreastshop.domain.product.item.model.dto.request.ItemSearchDto;
import com.daniel.mychickenbreastshop.domain.product.item.model.enums.ChickenStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemCustomQueryRepository {

    Page<Product> findItemWithDynamicQuery(Pageable pageable, ItemSearchDto itemSearchDto, ChickenCategory category, ChickenStatus status);

    Page<Product> findAllByCategoryName(ChickenCategory categoryName, Pageable pageable);
}
