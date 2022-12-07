package com.daniel.ddd.product.item.application.port.item.in;

import com.daniel.ddd.product.category.domain.enums.ChickenCategory;
import com.daniel.ddd.product.item.model.dto.request.ItemSearchDto;
import com.daniel.ddd.product.item.model.dto.response.DetailResponseDto;
import com.daniel.ddd.product.item.model.dto.response.ListResponseDto;
import com.daniel.ddd.product.item.domain.enums.ChickenStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemSearchUseCase {

    DetailResponseDto getProduct(Long productId);

    List<ListResponseDto> getAllProducts(ChickenCategory chickenCategory, Pageable pageable);

    List<ListResponseDto> searchProducts(Pageable pageable, ChickenStatus chickenStatus,
                                         ChickenCategory chickenCategory, ItemSearchDto itemSearchDto);
}
