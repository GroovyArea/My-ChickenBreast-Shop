package com.daniel.ddd.product.item.application.service;

import com.daniel.ddd.product.category.adaptor.out.persistence.CategoryRepository;
import com.daniel.ddd.product.item.adaptor.out.file.FileStore;
import com.daniel.ddd.product.item.adaptor.out.persistence.ProductRepository;
import com.daniel.ddd.product.item.application.dto.request.ModifyRequestDto;
import com.daniel.ddd.product.item.application.dto.request.RegisterRequestDto;
import com.daniel.ddd.product.item.application.port.item.in.ManageItemUseCase;
import com.daniel.mychickenbreastshop.domain.product.category.model.Category;
import com.daniel.mychickenbreastshop.domain.product.item.application.file.model.FileResponse;
import com.daniel.mychickenbreastshop.domain.product.item.mapper.ItemModifyMapper;
import com.daniel.mychickenbreastshop.domain.product.item.mapper.ItemRegisterMapper;
import com.daniel.mychickenbreastshop.domain.product.item.model.Product;
import com.daniel.mychickenbreastshop.domain.product.item.model.enums.ChickenStatus;
import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.daniel.mychickenbreastshop.domain.product.category.model.enums.ErrorMessages.CATEGORY_NOT_EXISTS;
import static com.daniel.mychickenbreastshop.domain.product.item.model.enums.ErrorMessages.ITEM_NOT_EXISTS;

@Service
@RequiredArgsConstructor
public class ManageItemService implements ManageItemUseCase {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final FileStore s3FileStore;
    private final ItemRegisterMapper itemRegisterMapper;
    private final ItemModifyMapper itemModifyMapper;

    @Override
    public Long registerItem(RegisterRequestDto registerRequestDto, MultipartFile file) {
        FileResponse uploadFileInfo = s3FileStore.upload(file);

        registerRequestDto.updateImageName(uploadFileInfo.getUploadFileUrl());

        Category dbCategory = categoryRepository.findByCategoryName(registerRequestDto.getCategory())
                .orElseThrow(() -> new BadRequestException(CATEGORY_NOT_EXISTS.getMessage()));

        Product savableProduct = itemRegisterMapper.toEntity(registerRequestDto);

        savableProduct.updateCategoryInfo(dbCategory);
        savableProduct.updateItemStatus(ChickenStatus.SALE);

        return productRepository.save(savableProduct).getId();    }

    @Override
    public void modifyItem(ModifyRequestDto modifyRequestDto) {
        Product dbProduct = productRepository.findById(modifyRequestDto.getId())
                .orElseThrow(() -> new BadRequestException(ITEM_NOT_EXISTS.getMessage()));
        Product updatableProduct = itemModifyMapper.toEntity(modifyRequestDto);
        Category updatableCategory = categoryRepository.findByCategoryName(modifyRequestDto.getCategory())
                .orElseThrow(() -> new BadRequestException(CATEGORY_NOT_EXISTS.getMessage()));

        dbProduct.updateProductInfo(updatableProduct);
        dbProduct.updateCategoryInfo(updatableCategory);
    }

    @Override
    public void removeItem(Long productId) {
        Product dbProduct = productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException(ITEM_NOT_EXISTS.getMessage()));
        dbProduct.remove();
    }

    @Override
    public void changeImage(Long productId, MultipartFile file) {
        Product dbProduct = productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException(ITEM_NOT_EXISTS.getMessage()));

        String savedFileName = dbProduct.getImage();
        s3FileStore.delete(savedFileName);

        FileResponse uploadFileInfo = s3FileStore.upload(file);
        dbProduct.updateImageInfo(uploadFileInfo.getUploadFileUrl());
    }
}
