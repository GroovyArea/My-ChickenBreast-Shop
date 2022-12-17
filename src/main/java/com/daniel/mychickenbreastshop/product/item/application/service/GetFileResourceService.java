package com.daniel.mychickenbreastshop.product.item.application.service;

import com.daniel.mychickenbreastshop.product.item.adaptor.out.file.FileStore;
import com.daniel.mychickenbreastshop.product.item.application.port.file.in.GetFileResourceUseCase;
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
