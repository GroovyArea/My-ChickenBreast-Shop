package com.daniel.mychickenbreastshop.domain.product.item.application.file;

import com.daniel.mychickenbreastshop.domain.product.item.application.file.model.FileResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * File Store 인터페이스
 */
public interface FileStore {

    /**
     * 파일 업로드
     *
     * @param multipartFile 업로드 할 파일
     * @return 파일 업로드 정보 응답
     */
    FileResponse upload(MultipartFile multipartFile);

    /**
     * 업로드 파일 삭제
     *
     * @param fileName 파일 이름
     */
    void delete(String fileName);

    /**
     * 파일 다운로드
     *
     * @param fileUrl 다운로드할 파일의 url
     * @return 파일 다운로드 정보 응답
     */
    FileResponse download(String fileUrl);
}
