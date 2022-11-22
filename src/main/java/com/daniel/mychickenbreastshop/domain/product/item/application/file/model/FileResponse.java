package com.daniel.mychickenbreastshop.domain.product.item.application.file.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class FileResponse {

    String uploadFileUrl;
    byte[] downloadFileResource;

}
