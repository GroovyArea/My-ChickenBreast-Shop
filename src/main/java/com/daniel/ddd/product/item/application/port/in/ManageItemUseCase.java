package com.daniel.ddd.product.item.application.port.in;

import com.daniel.ddd.product.item.application.dto.request.ModifyRequestDto;
import com.daniel.ddd.product.item.application.dto.request.RegisterRequestDto;
import org.springframework.web.multipart.MultipartFile;

public interface ManageItemUseCase {

    Long registerItem(RegisterRequestDto registerRequestDto, MultipartFile file);

    void modifyItem(ModifyRequestDto modifyRequestDto);

    void removeItem(Long productId);
}
