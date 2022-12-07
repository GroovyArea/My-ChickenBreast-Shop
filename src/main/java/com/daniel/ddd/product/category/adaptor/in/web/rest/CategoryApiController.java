package com.daniel.ddd.product.category.adaptor.in.web.rest;

import com.daniel.ddd.product.category.application.port.in.ManageCategoryUseCase;
import com.daniel.ddd.product.category.model.dto.request.ModifyRequestDto;
import com.daniel.ddd.product.category.model.dto.request.RegisterRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/categories")
public class CategoryApiController {

    private final ManageCategoryUseCase categoryUseCase;

    /**
     * 닭가슴살 카테고리 등록
     *
     * @param registerRequestDto 등록 dto
     */
    @PostMapping
    public ResponseEntity<Long> registerCategory(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        Long id = categoryUseCase.registerCategory(registerRequestDto);
        return ResponseEntity.ok().body(id);
    }

    /**
     * 닭가슴살 카테고리명 수정
     *
     * @param categoryId       카테고리 id
     * @param modifyRequestDto 수정 dto
     */
    @PatchMapping("/{categoryId}")
    public ResponseEntity<Void> modifyCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody ModifyRequestDto modifyRequestDto) {
        categoryUseCase.modifyCategory(categoryId, modifyRequestDto);
        return ResponseEntity.ok().build();
    }

}
