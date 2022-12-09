package com.daniel.mychickenbreastshop.product.item.adaptor.in.web.rest;

import com.daniel.mychickenbreastshop.product.category.domain.enums.ChickenCategory;
import com.daniel.mychickenbreastshop.product.item.model.dto.request.ItemSearchDto;
import com.daniel.mychickenbreastshop.product.item.model.dto.response.DetailResponseDto;
import com.daniel.mychickenbreastshop.product.item.model.dto.response.ListResponseDto;
import com.daniel.mychickenbreastshop.product.item.application.port.item.in.ItemSearchUseCase;
import com.daniel.mychickenbreastshop.product.item.domain.enums.ChickenStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 상품 컨트롤러
 *
 * <pre>
 *     <b>history</b>
 *     1.0 2022.08.23 최초 작성
 *     1.1 2022.09.28 조회 api 추가
 * </pre>
 *
 * @author Daniel Kim
 * @version 1.1
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductApiController {

    private final ItemSearchUseCase searchUseCase;

    /**
     * 단건 상품 조회
     *
     * @param productId 상품 id
     */
    @GetMapping("/{productId}")
    public ResponseEntity<DetailResponseDto> getProductDetail(@PathVariable Long productId) {
        DetailResponseDto product = searchUseCase.getProduct(productId);
        return ResponseEntity.ok(product);
    }

    /**
     * 상품 리스트 페이징 조회 (카테고리 별)
     *
     * @param category 카테고리
     */
    @GetMapping("/category")
    public ResponseEntity<List<ListResponseDto>> getProducts(@RequestParam(defaultValue = "STEAMED") ChickenCategory category,
                                                             @PageableDefault(page = 1, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(searchUseCase.getAllProducts(category, pageable));
    }

    /**
     * 상품 검색
     *
     * @param status      상품 상태
     * @param category    카테고리
     * @param searchKey   검색 조건
     * @param searchValue 검색 값
     */
    @GetMapping("/search/{category}")
    public ResponseEntity<List<ListResponseDto>> getSearchProducts(
            @PathVariable ChickenCategory category,
            @PageableDefault(page = 1, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(defaultValue = "SALE") ChickenStatus status,
            @RequestParam(defaultValue = "name") String searchKey,
            @RequestParam(defaultValue = "") String searchValue) {
        ItemSearchDto itemSearchDto = ItemSearchDto.builder()
                .searchKey(searchKey)
                .searchValue(searchValue)
                .build();
        return ResponseEntity.ok(searchUseCase.searchProducts(pageable, status, category, itemSearchDto));
    }
}
