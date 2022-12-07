package com.daniel.ddd.product.item.adaptor.out.file;

import com.daniel.mychickenbreastshop.domain.product.item.application.file.FileStore;
import com.daniel.mychickenbreastshop.domain.product.item.application.file.model.FileResponse;
import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import com.daniel.mychickenbreastshop.global.error.exception.InternalErrorException;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.daniel.mychickenbreastshop.domain.product.item.model.enums.ErrorMessages.CONTENT_TYPE_NOT_FOUND;
import static com.daniel.mychickenbreastshop.domain.product.item.model.enums.ErrorMessages.INVALID_FILE_CONTENT_TYPE;

@Component
public class LocalFileStore implements FileStore {

    @Value("${file.upload.location}")
    private String uploadDirectory;

    @Override
    public FileResponse upload(MultipartFile multipartFile) {
        String contentType = multipartFile.getContentType();

        if (ObjectUtils.isEmpty(contentType)) {
            throw new BadRequestException(CONTENT_TYPE_NOT_FOUND.getMessage());
        } else if (!(contentType.equals(ContentType.IMAGE_JPEG.toString())) ||
                contentType.equals(ContentType.IMAGE_PNG.toString())) {
            throw new BadRequestException(INVALID_FILE_CONTENT_TYPE.getMessage());
        }

        String formatDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("/yyyy-MM-dd HH:mm"));
        String uploadFileName = formatDate + multipartFile.getOriginalFilename();
        File uploadFile = new File(uploadDirectory, uploadFileName);

        try {
            multipartFile.transferTo(uploadFile);
        } catch (IOException e) {
            throw new InternalErrorException(e);
        }

        return FileResponse.builder()
                .uploadFileUrl(uploadFile.getAbsolutePath())
                .build();
    }

    @Override
    public void delete(String fileName) {
        String deletableFilePath = uploadDirectory + "/" + fileName;

        Path currentFileLocation = Paths.get(deletableFilePath)
                .toAbsolutePath().normalize();

        try {
            Files.delete(currentFileLocation);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public FileResponse download(String fileUrl) {
        String downloadFileDirectory = uploadDirectory + fileUrl;

        Path filePath = Paths.get(downloadFileDirectory)
                .toAbsolutePath().normalize();

        byte[] data;
        try {
            data = Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new InternalErrorException(e);
        }

        return FileResponse.builder()
                .downloadFileResource(data)
                .build();
    }

}
