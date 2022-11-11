package com.daniel.mychickenbreastshop.domain.product.category.api;

import com.daniel.mychickenbreastshop.domain.product.category.application.CategoryService;
import com.daniel.mychickenbreastshop.domain.product.category.model.dto.request.ModifyRequestDto;
import com.daniel.mychickenbreastshop.domain.product.category.model.dto.request.RegisterRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/categories")
public class CategoryApiController {

    private final CategoryService categoryService;

    /**
     * 닭가슴살 카테고리 등록
     * @param registerRequestDto 등록 dto
     */
    @PostMapping
    public ResponseEntity<Void> registerCategory(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        categoryService.registerCategory(registerRequestDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 닭가슴살 카테고리명 수정
     * @param categoryId 카테고리 id
     * @param modifyRequestDto 수정 dto
     */
    @PatchMapping("/{categoryId}")
    public ResponseEntity<Void> modifyCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody ModifyRequestDto modifyRequestDto) {
        categoryService.modifyCategory(categoryId, modifyRequestDto);
        return ResponseEntity.ok().build();
    }

}
