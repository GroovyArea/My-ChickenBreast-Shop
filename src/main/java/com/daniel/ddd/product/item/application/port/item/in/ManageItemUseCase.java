package com.daniel.ddd.product.item.application.port.item.in;

import com.daniel.ddd.product.item.model.dto.request.ModifyRequestDto;
import com.daniel.ddd.product.item.model.dto.request.RegisterRequestDto;
import org.springframework.web.multipart.MultipartFile;

public interface ManageItemUseCase {

    Long registerItem(RegisterRequestDto registerRequestDto, MultipartFile file);

    void modifyItem(ModifyRequestDto modifyRequestDto);

    void removeItem(Long productId);

    void changeImage(Long productId, MultipartFile file);
}
