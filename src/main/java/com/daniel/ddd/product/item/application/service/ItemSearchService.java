package com.daniel.ddd.product.item.application.service;

import com.daniel.ddd.global.error.exception.BadRequestException;
import com.daniel.ddd.product.category.domain.enums.ChickenCategory;
import com.daniel.ddd.product.item.adaptor.out.persistence.ProductRepository;
import com.daniel.ddd.product.item.application.dto.request.ItemSearchDto;
import com.daniel.ddd.product.item.application.dto.response.DetailResponseDto;
import com.daniel.ddd.product.item.application.dto.response.ListResponseDto;
import com.daniel.ddd.product.item.application.port.in.ItemSearchUseCase;
import com.daniel.ddd.product.item.domain.enums.ChickenStatus;
import com.daniel.ddd.product.item.domain.enums.ErrorMessages;
import com.daniel.mychickenbreastshop.domain.product.item.mapper.ItemDetailMapper;
import com.daniel.mychickenbreastshop.domain.product.item.mapper.ItemListMapper;
import com.daniel.mychickenbreastshop.domain.product.item.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemSearchService implements ItemSearchUseCase {

    private final ProductRepository productRepository;
    private ItemDetailMapper itemDetailMapper;
    private ItemListMapper itemListMapper;

    @Override
    public DetailResponseDto getProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.ITEM_NOT_EXISTS.getMessage()));

        return itemDetailMapper.toDTO(product);
    }

    @Override
    public List<ListResponseDto> getAllProducts(ChickenCategory chickenCategory, Pageable pageable) {
        List<Product> products = productRepository.findAllByCategoryName(category, pageable)
                .getContent();

        return products.stream()
                .map(itemListMapper::toDTO)
                .toList();
    }

    @Override
    public List<ListResponseDto> searchProducts(Pageable pageable, ChickenStatus chickenStatus, ChickenCategory chickenCategory, ItemSearchDto itemSearchDto) {
        List<Product> searchedItems = productRepository.findItemWithDynamicQuery(
                pageable, searchDto, category, status).getContent();

        return searchedItems.stream()
                .map(itemListMapper::toDTO)
                .toList();
    }
}
