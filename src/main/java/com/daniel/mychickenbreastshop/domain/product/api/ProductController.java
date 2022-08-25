package com.daniel.mychickenbreastshop.domain.product.api;

import com.daniel.mychickenbreastshop.domain.product.application.ProductService;
import com.daniel.mychickenbreastshop.domain.product.domain.category.ChickenCategory;
import com.daniel.mychickenbreastshop.domain.product.domain.item.dto.response.DetailResponseDto;
import com.daniel.mychickenbreastshop.domain.product.domain.item.dto.response.ListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
public class ProductController {

    private final ProductService productService;

    @GetMapping("/v1/products/{productId}")
    public ResponseEntity<DetailResponseDto> getProductDetail(@PathVariable Long productId) {
        DetailResponseDto product = productService.getProduct(productId);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/v1/products/{categoryName}")
    public ResponseEntity<List<ListResponseDto>> getProductList(@PathVariable ChickenCategory categoryName,
                                                                @PageableDefault(size = 8, sort = "productId", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProduct(categoryName.name(), pageable));
    }

    @GetMapping("/v1/products/download/{fileName}")
    public ResponseEntity<Resource> getDownloadFile(@PathVariable String fileName, HttpServletRequest request) {
        productService.getFileResource(fileName);
    }

}
