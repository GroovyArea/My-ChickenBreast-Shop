package com.daniel.mychickenbreastshop.domain.product.item.application.file;

import com.daniel.mychickenbreastshop.domain.product.item.application.file.model.UploadFileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStore {

    UploadFileResponse uploadFile(MultipartFile file) throws IOException;

    byte[] downloadFile(String fileKey) throws IOException;
}
