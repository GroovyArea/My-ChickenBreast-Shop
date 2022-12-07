package com.daniel.ddd.product.item.adaptor.in.web.rest;

import com.daniel.ddd.product.item.application.dto.request.ModifyRequestDto;
import com.daniel.ddd.product.item.application.dto.request.RegisterRequestDto;
import com.daniel.ddd.product.item.application.port.item.in.ManageItemUseCase;
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

    private final ManageItemUseCase itemUseCase;

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
        Long productId = itemUseCase.registerItem(registerRequestDto, file);
        return ResponseEntity.ok().body(productId);
    }

    /**
     * 상품 수정
     *
     * @param modifyRequestDto 수정 Dto
     */
    @PatchMapping
    public ResponseEntity<Void> modifyProduct(@RequestBody ModifyRequestDto modifyRequestDto) {
        itemUseCase.modifyItem(modifyRequestDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 상품 삭제
     *
     * @param productId 상품 고유 번호
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeProduct(@PathVariable Long productId) {
        itemUseCase.removeItem(productId);
        return ResponseEntity.ok().build();
    }

}
