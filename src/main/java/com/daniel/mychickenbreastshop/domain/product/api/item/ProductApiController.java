package com.daniel.mychickenbreastshop.domain.product.api.item;

import com.daniel.mychickenbreastshop.domain.product.application.ProductService;
import com.daniel.mychickenbreastshop.domain.product.model.category.enums.ChickenCategory;
import com.daniel.mychickenbreastshop.domain.product.model.item.dto.request.ItemSearchDto;
import com.daniel.mychickenbreastshop.domain.product.model.item.dto.request.ModifyRequestDto;
import com.daniel.mychickenbreastshop.domain.product.model.item.dto.request.RegisterRequestDto;
import com.daniel.mychickenbreastshop.domain.product.model.item.dto.response.DetailResponseDto;
import com.daniel.mychickenbreastshop.domain.product.model.item.dto.response.ListResponseDto;
import com.daniel.mychickenbreastshop.domain.product.model.item.enums.ChickenStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
@RequestMapping("/api")
public class ProductApiController {

    private final ProductService productService;

    /**
     * 단건 상품 조회
     *
     * @param productId 상품 id
     */
    @GetMapping("/v1/products/{productId}")
    public ResponseEntity<DetailResponseDto> getProductDetail(@PathVariable Long productId) {
        DetailResponseDto product = productService.getProduct(productId);
        return ResponseEntity.ok(product);
    }

    /**
     * 상품 리스트 페이징 조회 (카테고리 별)
     *
     * @param category 카테고리
     * @param page     페이지 번호
     */
    @GetMapping("/v1/products/category")
    public ResponseEntity<List<ListResponseDto>> getProducts(@RequestParam(defaultValue = "STEAMED") ChickenCategory category,
                                                             @RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.ok(productService.getAllProduct(category, page));
    }

    /**
     * 상품 검색
     *
     * @param page        페이지 번호
     * @param status      상품 상태
     * @param category    카테고리
     * @param searchKey   검색 조건
     * @param searchValue 검색 값
     */
    @GetMapping("/v2/products/search/{category}")
    public ResponseEntity<List<ListResponseDto>> getSearchProducts(
            @PathVariable ChickenCategory category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "SALE") ChickenStatus status,
            @RequestParam(defaultValue = "name") String searchKey,
            @RequestParam(defaultValue = "") String searchValue) {
        ItemSearchDto itemSearchDto = ItemSearchDto.builder()
                .searchKey(searchKey)
                .searchValue(searchValue)
                .build();
        return ResponseEntity.ok(productService.searchProducts(page, status, category, itemSearchDto));
    }

    /**
     * 상품 등록
     *
     * @param registerRequestDto 등록 Dto
     * @param file               이미지 파일
     * @return 상품 고유 번호
     */
    @PostMapping("/v2/products")
    public ResponseEntity<Long> registerProduct(@RequestPart RegisterRequestDto registerRequestDto,
                                                @RequestPart(value = "image") MultipartFile file) {
        Long productId = productService.registerItem(registerRequestDto, file);
        return ResponseEntity.ok().body(productId);
    }

    /**
     * 상품 수정
     *
     * @param modifyRequestDto 수정 Dto
     */
    @PatchMapping("/v2/products")
    public ResponseEntity<Void> modifyProduct(@RequestBody ModifyRequestDto modifyRequestDto) {
        productService.modifyItem(modifyRequestDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 상품 삭제
     *
     * @param productId 상품 고유 번호
     */
    @DeleteMapping("/v2/products/{productId}")
    public ResponseEntity<Void> removeProduct(@PathVariable Long productId) {
        productService.removeItem(productId);
        return ResponseEntity.ok().build();
    }

}
