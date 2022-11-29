package com.daniel.mychickenbreastshop.domain.product.item.application;

import com.daniel.mychickenbreastshop.domain.product.category.model.Category;
import com.daniel.mychickenbreastshop.domain.product.category.model.CategoryRepository;
import com.daniel.mychickenbreastshop.domain.product.category.model.enums.ChickenCategory;
import com.daniel.mychickenbreastshop.domain.product.item.application.file.FileStore;
import com.daniel.mychickenbreastshop.domain.product.item.application.file.model.FileResponse;
import com.daniel.mychickenbreastshop.domain.product.item.mapper.ItemDetailMapper;
import com.daniel.mychickenbreastshop.domain.product.item.mapper.ItemListMapper;
import com.daniel.mychickenbreastshop.domain.product.item.mapper.ItemModifyMapper;
import com.daniel.mychickenbreastshop.domain.product.item.mapper.ItemRegisterMapper;
import com.daniel.mychickenbreastshop.domain.product.item.model.Product;
import com.daniel.mychickenbreastshop.domain.product.item.model.ProductRepository;
import com.daniel.mychickenbreastshop.domain.product.item.model.dto.request.ItemSearchDto;
import com.daniel.mychickenbreastshop.domain.product.item.model.dto.request.ModifyRequestDto;
import com.daniel.mychickenbreastshop.domain.product.item.model.dto.request.RegisterRequestDto;
import com.daniel.mychickenbreastshop.domain.product.item.model.dto.response.DetailResponseDto;
import com.daniel.mychickenbreastshop.domain.product.item.model.dto.response.ListResponseDto;
import com.daniel.mychickenbreastshop.domain.product.item.model.enums.ChickenStatus;
import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.daniel.mychickenbreastshop.domain.product.category.model.enums.ErrorMessages.CATEGORY_NOT_EXISTS;
import static com.daniel.mychickenbreastshop.domain.product.item.model.enums.ErrorMessages.INVALID_PAY_AMOUNT;
import static com.daniel.mychickenbreastshop.domain.product.item.model.enums.ErrorMessages.ITEM_NOT_EXISTS;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final FileStore s3FileStore;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ItemDetailMapper itemDetailMapper;
    private final ItemListMapper itemListMapper;
    private final ItemRegisterMapper itemRegisterMapper;
    private final ItemModifyMapper itemModifyMapper;

    /**
     * 상품 단건 조회
     */
    @Transactional(readOnly = true)
    public DetailResponseDto getProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException(ITEM_NOT_EXISTS.getMessage()));
        return itemDetailMapper.toDTO(product);
    }

    /**
     * 상품 목록 조회
     */
    @Transactional(readOnly = true)
    public List<ListResponseDto> getAllProduct(ChickenCategory category, Pageable pageable) {
        List<Product> products = productRepository.findAllByCategoryName(category, pageable)
                .getContent();

        return products.stream()
                .map(itemListMapper::toDTO)
                .toList();
    }

    /**
     * 검색 조건에 따른 상품 목록 10개 반환
     */
    @Transactional(readOnly = true)
    public List<ListResponseDto> searchProducts(Pageable pageable, ChickenStatus status, ChickenCategory category, ItemSearchDto searchDto) {
        List<Product> searchedItems = productRepository.findItemWithDynamicQuery(
                pageable, searchDto, category, status).getContent();

        return searchedItems.stream()
                .map(itemListMapper::toDTO)
                .toList();
    }

    /**
     * 상품 등록
     */
    @Transactional
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

    /**
     * 상품 수정
     */
    @Transactional
    public void modifyItem(ModifyRequestDto modifyRequestDto) {
        Product dbProduct = productRepository.findById(modifyRequestDto.getId())
                .orElseThrow(() -> new BadRequestException(ITEM_NOT_EXISTS.getMessage()));
        Product updatableProduct = itemModifyMapper.toEntity(modifyRequestDto);
        Category updatableCategory = categoryRepository.findByCategoryName(modifyRequestDto.getCategory())
                .orElseThrow(() -> new BadRequestException(CATEGORY_NOT_EXISTS.getMessage()));

        dbProduct.updateProductInfo(updatableProduct);
        dbProduct.updateCategoryInfo(updatableCategory);
    }

    /**
     * 상품 삭제 처리
     */
    @Transactional
    public void removeItem(Long productId) {
        Product dbProduct = productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException(ITEM_NOT_EXISTS.getMessage()));
        dbProduct.updateItemStatus(ChickenStatus.EXTINCTION);
        dbProduct.delete();
    }

    /**
     * 상품 이미지 파일 변경
     */
    @Transactional
    public void changeImage(Long productId, MultipartFile file) {
        Product dbProduct = productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException(ITEM_NOT_EXISTS.getMessage()));

        String savedFileName = dbProduct.getImage();
        s3FileStore.delete(savedFileName);

        FileResponse uploadFileInfo = s3FileStore.upload(file);
        dbProduct.updateImageInfo(uploadFileInfo.getUploadFileUrl());
    }


    /**
     * 결제 상품 가격 정보 유효성 검사
     */
    @Transactional(readOnly = true)
    public void validatePayAmount(Long itemNo, int itemQuantity, long totalPrice) {
        Product dbProduct = productRepository.findById(itemNo)
                .orElseThrow(() -> new BadRequestException(ITEM_NOT_EXISTS.getMessage()));

        if ((long) dbProduct.getPrice() * itemQuantity != totalPrice) {
            throw new BadRequestException(INVALID_PAY_AMOUNT.getMessage());
        }
    }

    /**
     * 이미지 파일의 byte resource 반환
     */
    public byte[] getFileByteResource(String fileUrl) {
        return s3FileStore.download(fileUrl).getDownloadFileResource();
    }
}
