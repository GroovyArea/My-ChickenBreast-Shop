package com.daniel.mychickenbreastshop.domain.product.item.application.file;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.daniel.mychickenbreastshop.domain.product.item.application.file.model.FileResponse;
import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import com.daniel.mychickenbreastshop.global.error.exception.InternalErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.daniel.mychickenbreastshop.domain.product.item.model.enums.ErrorMessages.CONTENT_TYPE_NOT_FOUND;
import static com.daniel.mychickenbreastshop.domain.product.item.model.enums.ErrorMessages.INVALID_FILE_CONTENT_TYPE;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3FileStore implements FileStore {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("{file.upload.location}")
    private String uploadDirectory;

    private final AmazonS3Client s3Client;

    @Override
    public FileResponse upload(MultipartFile multipartFile) {
        String contentType = multipartFile.getContentType();

        if (ObjectUtils.isEmpty(contentType)) {
            throw new BadRequestException(CONTENT_TYPE_NOT_FOUND.getMessage());
        } else if (!(contentType.equals(ContentType.IMAGE_JPEG.toString())) ||
                contentType.equals(ContentType.IMAGE_PNG.toString())) {
            throw new BadRequestException(INVALID_FILE_CONTENT_TYPE.getMessage());
        }

        File uploadFile = convertToFile(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("File 전환에 실패하였습니다."));

        String formatDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("/yyyy-MM-dd HH:mm"));
        String uploadFileName = uploadDirectory + formatDate + uploadFile.getName();

        String uploadFileUrl = putFileToS3(uploadFile, uploadFileName);

        removeFile(uploadFile);

        return FileResponse.builder()
                .uploadFileUrl(uploadFileUrl)
                .build();
    }

    @Override
    public void delete(String fileName) {
        String deleteFileKey = uploadDirectory + fileName;
        s3Client.deleteObject(new DeleteObjectRequest(bucket, deleteFileKey));
    }

    @Override
    public FileResponse download(String fileUrl) {
        S3Object s3ClientObject = s3Client.getObject(new GetObjectRequest(bucket, fileUrl));
        S3ObjectInputStream objectInputStream = s3ClientObject.getObjectContent();

        byte[] bytes;
        try {
            bytes = IOUtils.toByteArray(objectInputStream);
        } catch (IOException e) {
            throw new InternalErrorException(e);
        }

        return FileResponse.builder()
                .downloadFileResource(bytes)
                .build();
    }

    private Optional<File> convertToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try {
            file.transferTo(convertedFile);
        } catch (IOException e) {
            throw new InternalErrorException(e);
        }
        return Optional.of(convertedFile);
    }

    private String putFileToS3(File uploadFile, String fileName) {
        s3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return s3Client.getUrl(bucket, fileName).toString();
    }

    private void removeFile(File targetFile) {
        Path path = targetFile.toPath();

        if (targetFile.exists()) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new InternalErrorException(e);
            }
        }
    }
}
