package com.daniel.ddd.product.item.application.service;

import com.daniel.ddd.product.item.application.port.in.file.FileStore;
import com.daniel.ddd.product.item.application.port.in.file.usecase.GetFileResourceUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetFileResourceService implements GetFileResourceUseCase {

    private final FileStore s3FileStore;

    @Override
    public byte[] getFileOfByteResource(String fileUrl) {
        return s3FileStore.download(fileUrl).getDownloadFileResource();
    }
}
