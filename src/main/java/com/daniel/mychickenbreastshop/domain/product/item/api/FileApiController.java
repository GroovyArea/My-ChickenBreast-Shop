package com.daniel.mychickenbreastshop.domain.product.item.api;

import com.daniel.mychickenbreastshop.domain.product.item.application.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
     * @param fileName 업로드 된 이미지 파일 이름
     * @return 파일 리소스
     */
    @GetMapping("/download/{fileName}")
    public ResponseEntity<byte[]> getDownloadFile(@PathVariable String fileName) {
        byte[] fileByteResource = productService.getFileByteResource(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename\"" + URLEncoder.encode(fileName, StandardCharsets.UTF_8))
                .body(fileByteResource);
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
