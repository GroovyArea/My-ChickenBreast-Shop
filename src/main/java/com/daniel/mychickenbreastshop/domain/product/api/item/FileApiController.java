package com.daniel.mychickenbreastshop.domain.product.api.item;

import com.daniel.mychickenbreastshop.domain.product.application.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 파일 API 컨트롤러
 *
 * <pre>
 *     <b>history</b>
 *     1.0 2022.11.06 최초 작성
 * </pre>
 *
 * @author Daniel Kim
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/files")
public class FileApiController {

    private final ProductService productService;

    /**
     * 상품 이미지 파일 다운로드
     *
     * @param fileName 이미지 파일 이름
     * @param request  HttpServletRequest
     * @return 파일 리소스
     */
    @GetMapping("/download/{fileName}")
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
     * 상품 이미지 변경
     *
     * @param productId 상품 아이디
     * @param file      변경할 이미지 파일
     * @return OK
     */
    @PostMapping("/{id}")
    public ResponseEntity<Void> modifyFileOfItem(@PathVariable(value = "id") Long productId, @RequestPart("image") MultipartFile file) {
        productService.changeImage(productId, file);
        return ResponseEntity.ok().build();
    }
}
