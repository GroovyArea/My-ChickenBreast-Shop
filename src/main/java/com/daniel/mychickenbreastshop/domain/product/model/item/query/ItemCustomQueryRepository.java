package com.daniel.mychickenbreastshop.domain.product.model.item.query;

import com.daniel.mychickenbreastshop.domain.product.model.category.model.ChickenCategory;
import com.daniel.mychickenbreastshop.domain.product.model.item.Product;
import com.daniel.mychickenbreastshop.domain.product.model.item.dto.request.ItemSearchDto;
import com.daniel.mychickenbreastshop.domain.product.model.item.model.ChickenStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemCustomQueryRepository {

    Page<Product> findItemWithDynamicQuery(Pageable pageable, ItemSearchDto itemSearchDto, ChickenCategory category, ChickenStatus status);

    Page<Product> findAllByCategoryName(ChickenCategory categoryName, Pageable pageable);
}
