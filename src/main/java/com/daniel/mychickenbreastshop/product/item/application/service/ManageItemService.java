package com.daniel.mychickenbreastshop.product.item.application.service;

import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import com.daniel.mychickenbreastshop.product.category.adaptor.out.persistence.CategoryRepository;
import com.daniel.mychickenbreastshop.product.category.domain.Category;
import com.daniel.mychickenbreastshop.product.item.adaptor.out.file.FileStore;
import com.daniel.mychickenbreastshop.product.item.adaptor.out.persistence.ProductRepository;
import com.daniel.mychickenbreastshop.product.item.application.port.file.out.model.FileResponse;
import com.daniel.mychickenbreastshop.product.item.application.port.item.in.ManageItemUseCase;
import com.daniel.mychickenbreastshop.product.item.domain.Product;
import com.daniel.mychickenbreastshop.product.item.domain.enums.ChickenStatus;
import com.daniel.mychickenbreastshop.product.item.mapper.ItemModifyMapper;
import com.daniel.mychickenbreastshop.product.item.mapper.ItemRegisterMapper;
import com.daniel.mychickenbreastshop.product.item.model.dto.request.ModifyRequestDto;
import com.daniel.mychickenbreastshop.product.item.model.dto.request.RegisterRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.daniel.mychickenbreastshop.product.category.domain.enums.ErrorMessages.CATEGORY_NOT_EXISTS;
import static com.daniel.mychickenbreastshop.product.item.domain.enums.ErrorMessages.ITEM_NOT_EXISTS;

@Service
@RequiredArgsConstructor
@Transactional
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

        return productRepository.save(savableProduct).getId();
    }

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
