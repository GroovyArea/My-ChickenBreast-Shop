package com.daniel.mychickenbreastshop.domain.admin.api;

import com.daniel.mychickenbreastshop.domain.product.item.application.ProductService;
import com.daniel.mychickenbreastshop.domain.product.item.model.dto.request.ModifyRequestDto;
import com.daniel.mychickenbreastshop.domain.product.item.model.dto.request.RegisterRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 관리자용 상품 API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/products")
public class ProductAdminApiController {

    private final ProductService productService;

    /**
     * 상품 등록
     *
     * @param registerRequestDto 등록 Dto
     * @param file               이미지 파일
     * @return 상품 고유 번호
     */
    @PostMapping
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
    @PatchMapping
    public ResponseEntity<Void> modifyProduct(@RequestBody ModifyRequestDto modifyRequestDto) {
        productService.modifyItem(modifyRequestDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 상품 삭제
     *
     * @param productId 상품 고유 번호
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeProduct(@PathVariable Long productId) {
        productService.removeItem(productId);
        return ResponseEntity.ok().build();
    }

}
