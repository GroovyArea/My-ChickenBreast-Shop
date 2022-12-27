package com.daniel.mychickenbreastshop.product.item.application.service;

import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import com.daniel.mychickenbreastshop.product.category.domain.enums.ChickenCategory;
import com.daniel.mychickenbreastshop.product.item.adaptor.out.persistence.ProductRepository;
import com.daniel.mychickenbreastshop.product.item.application.port.item.in.ItemSearchUseCase;
import com.daniel.mychickenbreastshop.product.item.domain.Product;
import com.daniel.mychickenbreastshop.product.item.domain.enums.ErrorMessages;
import com.daniel.mychickenbreastshop.product.item.mapper.ItemDetailMapper;
import com.daniel.mychickenbreastshop.product.item.mapper.ItemListMapper;
import com.daniel.mychickenbreastshop.product.item.model.dto.request.ItemSearchDto;
import com.daniel.mychickenbreastshop.product.item.model.dto.response.DetailResponseDto;
import com.daniel.mychickenbreastshop.product.item.model.dto.response.ListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemSearchService implements ItemSearchUseCase {

    private final ProductRepository productRepository;
    private final ItemDetailMapper itemDetailMapper;
    private final ItemListMapper itemListMapper;

    @Override
    public DetailResponseDto getProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.ITEM_NOT_EXISTS.getMessage()));

        return itemDetailMapper.toDTO(product);
    }

    @Override
    public List<ListResponseDto> getAllProducts(ChickenCategory chickenCategory, Pageable pageable) {
        List<Product> products = productRepository.findAllByCategoryName(chickenCategory, pageable)
                .getContent();

        return products.stream()
                .map(itemListMapper::toDTO)
                .toList();
    }

    @Override
    public List<ListResponseDto> searchProducts(Pageable pageable, ChickenCategory chickenCategory,
                                                ItemSearchDto itemSearchDto) {
        List<Product> searchedItems = productRepository.findItemWithDynamicQuery(
                pageable, itemSearchDto, chickenCategory).getContent();

        return searchedItems.stream()
                .map(itemListMapper::toDTO)
                .toList();
    }
}
