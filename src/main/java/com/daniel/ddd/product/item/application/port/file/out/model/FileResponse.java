package com.daniel.ddd.product.item.application.port.file.out.model;

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
