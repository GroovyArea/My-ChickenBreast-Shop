package com.daniel.mychickenbreastshop.domain.product.application;

import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 파일 관리 작업 클래스
 */
@Component
public class FileManager {

    private static final String FILE_NOT_FOUND = "파일을 찾을 수 없습니다.";

    @Value("${file.upload.location}")
    private String uploadDirectory;

    public String uploadFile(MultipartFile imageFile) {
        String originalFileName = imageFile.getOriginalFilename();

        UUID uuid = UUID.randomUUID();

        String uploadFileName = uuid + "_" + originalFileName;

        File uploadFile = new File(uploadDirectory, uploadFileName);

        try {
            imageFile.transferTo(uploadFile);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        return uploadFileName;
    }

    public void deleteFile(String imageFileName) {
        String deletableDirectory = uploadDirectory + "/" + imageFileName;

        Path currentFileLocation = Paths.get(deletableDirectory)
                .toAbsolutePath().normalize();
        try {
            Files.delete(currentFileLocation);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public Resource loadFile(String imageFileName) {
        try {
            Path directoryLocation = Paths.get(uploadDirectory)
                    .toAbsolutePath().normalize();

            Path file = directoryLocation.resolve(imageFileName).normalize();
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new BadRequestException(FILE_NOT_FOUND);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getDownloadURI(String uploadFileName) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/products/download/")
                .path(uploadFileName)
                .toUriString();
    }
}
