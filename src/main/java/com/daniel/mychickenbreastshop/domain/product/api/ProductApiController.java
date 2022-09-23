package com.daniel.mychickenbreastshop.domain.product.api;

import com.daniel.mychickenbreastshop.domain.product.application.ProductService;
import com.daniel.mychickenbreastshop.domain.product.domain.category.ChickenCategory;
import com.daniel.mychickenbreastshop.domain.product.domain.item.dto.request.ModifyRequestDto;
import com.daniel.mychickenbreastshop.domain.product.domain.item.dto.request.RegisterRequestDto;
import com.daniel.mychickenbreastshop.domain.product.domain.item.dto.response.DetailResponseDto;
import com.daniel.mychickenbreastshop.domain.product.domain.item.dto.response.ListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
 * </pre>
 *
 * @author Daniel Kim
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductApiController {

    private final ProductService productService;

    /**
     * 단건 상품 조회
     *
     * @param productId 상품 고유 번호
     * @return 상품 디테일
     */
    @GetMapping("/v1/products/{productId}")
    public ResponseEntity<DetailResponseDto> getProductDetail(@PathVariable Long productId) {
        DetailResponseDto product = productService.getProduct(productId);
        return ResponseEntity.ok(product);
    }

    /**
     * 상품 리스트 페이징 조회
     *
     * @param categoryName 카테고리명
     * @param pageable     페이지네이션
     * @return 상품 리스트
     */
    @GetMapping("/v1/products/search/{categoryName}")
    public ResponseEntity<List<ListResponseDto>> getProductList(@PathVariable ChickenCategory categoryName,
                                                                @PageableDefault(size = 8, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProduct(categoryName, pageable));
    }

    /**
     * 상품 이미지 파일 다운로드
     *
     * @param fileName 이미지 파일 이름
     * @param request  HttpServletRequest
     * @return 파일 리소스
     */
    @GetMapping("/v1/products/download/{fileName}")
    public ResponseEntity<Resource> getDownloadFile(@PathVariable String fileName, javax.servlet.http.HttpServletRequest request) {
        Resource resource = productService.getItemImageResource(fileName);
        String imageFilePath = productService.getItemFilePath(resource);
        String contentType = request.getServletContext().getMimeType(imageFilePath);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename\"" + resource.getFilename())
                .body(resource);
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
        if (file == null) {
            return ResponseEntity.badRequest().build();
        }

        Long productId = productService.registerItem(registerRequestDto, file);
        return ResponseEntity.ok().body(productId);
    }

    /**
     * 상품 수정
     *
     * @param modifyRequestDto 수정 Dto
     * @param file             이미지 파일
     */
    @PatchMapping("/v2/products")
    public ResponseEntity<Void> modifyProduct(@RequestPart ModifyRequestDto modifyRequestDto,
                                              @RequestPart("image") MultipartFile file) {
        productService.modifyItem(modifyRequestDto, file);
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
