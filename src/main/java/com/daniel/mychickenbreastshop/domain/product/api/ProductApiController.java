package com.daniel.mychickenbreastshop.domain.product.api;

import com.daniel.mychickenbreastshop.domain.payment.application.strategy.service.KakaopayStrategyApplication;
import com.daniel.mychickenbreastshop.domain.product.application.ProductService;
import com.daniel.mychickenbreastshop.domain.product.model.category.model.ChickenCategory;
import com.daniel.mychickenbreastshop.domain.product.model.item.dto.request.ItemSearchDto;
import com.daniel.mychickenbreastshop.domain.product.model.item.dto.request.ModifyRequestDto;
import com.daniel.mychickenbreastshop.domain.product.model.item.dto.request.RegisterRequestDto;
import com.daniel.mychickenbreastshop.domain.product.model.item.dto.response.DetailResponseDto;
import com.daniel.mychickenbreastshop.domain.product.model.item.dto.response.ListResponseDto;
import com.daniel.mychickenbreastshop.domain.product.model.item.model.ChickenStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
@Slf4j
public class ProductApiController {

    private final ProductService productService;
    private final KakaopayStrategyApplication kakaopayStrategyApplication;

    /**
     * 단건 상품 조회
     * @param productId 상품 id
     */
    @GetMapping("/v1/products/{productId}")
    public ResponseEntity<DetailResponseDto> getProductDetail(@PathVariable Long productId) {
        DetailResponseDto product = productService.getProduct(productId);
        return ResponseEntity.ok(product);
    }

    /**
     * 상품 리스트 페이징 조회 (카테고리 별)
     * @param category 카테고리
     * @param page 페이지 번호
     */
    @GetMapping("/v1/products/category")
    public ResponseEntity<List<ListResponseDto>> getProducts(@RequestParam(defaultValue = "BALL") ChickenCategory category,
                                                             @RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.ok(productService.getAllProduct(category, page));
    }

    /**
     * 상품 검색
     * @param page 페이지 번호
     * @param status 상품 상태
     * @param category 카테고리
     * @param searchDto 검색 조건
     */
    @GetMapping("/v2/products/search")
    public ResponseEntity<List<ListResponseDto>> getSearchProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "SALE") ChickenStatus status,
            @RequestParam(defaultValue = "STEAMED") ChickenCategory category,
            @RequestBody ItemSearchDto searchDto) {

        return ResponseEntity.ok(productService.searchProducts(page, status, category, searchDto));
    }

    /**
     * 상품 이미지 파일 다운로드
     *
     * @param fileName 이미지 파일 이름
     * @param request  HttpServletRequest
     * @return 파일 리소스
     */
    @GetMapping("/v1/products/download/{fileName}")
    public ResponseEntity<Resource> getDownloadFile(@PathVariable String fileName, HttpServletRequest request) {
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

    @GetMapping("/test")
    public void test() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            int finalI = i;
            executorService.submit(() -> {
                try {
                    log.info(finalI +"번째 일꾼 일한다.");
                    kakaopayStrategyApplication.test("lala");
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

    }

}
