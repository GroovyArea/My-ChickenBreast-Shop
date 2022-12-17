package com.daniel.mychickenbreastshop.product.item.application.port.item.in;

import com.daniel.mychickenbreastshop.product.category.domain.enums.ChickenCategory;
import com.daniel.mychickenbreastshop.product.item.model.dto.request.ItemSearchDto;
import com.daniel.mychickenbreastshop.product.item.model.dto.response.DetailResponseDto;
import com.daniel.mychickenbreastshop.product.item.model.dto.response.ListResponseDto;
import com.daniel.mychickenbreastshop.product.item.domain.enums.ChickenStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemSearchUseCase {

    DetailResponseDto getProduct(Long productId);

    List<ListResponseDto> getAllProducts(ChickenCategory chickenCategory, Pageable pageable);

    List<ListResponseDto> searchProducts(Pageable pageable, ChickenStatus chickenStatus,
                                         ChickenCategory chickenCategory, ItemSearchDto itemSearchDto);
}
