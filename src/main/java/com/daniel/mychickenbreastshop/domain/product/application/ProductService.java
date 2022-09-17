package com.daniel.mychickenbreastshop.domain.product.application;

import com.daniel.mychickenbreastshop.domain.product.domain.category.Category;
import com.daniel.mychickenbreastshop.domain.product.domain.category.CategoryRepository;
import com.daniel.mychickenbreastshop.domain.product.domain.category.ChickenCategory;
import com.daniel.mychickenbreastshop.domain.product.domain.category.model.CategoryResponse;
import com.daniel.mychickenbreastshop.domain.product.domain.item.Product;
import com.daniel.mychickenbreastshop.domain.product.domain.item.ProductRepository;
import com.daniel.mychickenbreastshop.domain.product.domain.item.dto.request.ModifyRequestDto;
import com.daniel.mychickenbreastshop.domain.product.domain.item.dto.request.RegisterRequestDto;
import com.daniel.mychickenbreastshop.domain.product.domain.item.dto.response.DetailResponseDto;
import com.daniel.mychickenbreastshop.domain.product.domain.item.dto.response.ListResponseDto;
import com.daniel.mychickenbreastshop.domain.product.domain.item.model.ChickenStatus;
import com.daniel.mychickenbreastshop.domain.product.domain.item.model.ProductResponse;
import com.daniel.mychickenbreastshop.domain.product.mapper.ItemDetailMapper;
import com.daniel.mychickenbreastshop.domain.product.mapper.ItemListMapper;
import com.daniel.mychickenbreastshop.domain.product.mapper.ItemModifyMapper;
import com.daniel.mychickenbreastshop.domain.product.mapper.ItemRegisterMapper;
import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import com.daniel.mychickenbreastshop.global.error.exception.InternalErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final FileManager fileManager;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ItemDetailMapper itemDetailMapper;
    private final ItemListMapper itemListMapper;
    private final ItemRegisterMapper itemRegisterMapper;
    private final ItemModifyMapper itemModifyMapper;

    // 상품 단건 조회
    @Transactional(readOnly = true)
    public DetailResponseDto getProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException(ProductResponse.ITEM_NOT_EXISTS.getMessage()));

        String downLoadURI = fileManager.getDownloadURI(product.getImage());

        DetailResponseDto dto = itemDetailMapper.toDTO(product);

        dto.setImage(downLoadURI);

        return dto;
    }

    // 상품 리스트 조회
    @Transactional(readOnly = true)
    public List<ListResponseDto> getAllProduct(ChickenCategory categoryName, Pageable pageable) {
        return productRepository.findByCategoryNameUsingFetchJoin(categoryName, pageable).stream()
                .map(itemListMapper::toDTO)
                .toList();
    }

    public Resource getItemImageResource(String fileName) {
        return fileManager.loadFile(fileName);
    }

    public String getItemFilePath(Resource resource) {
        try {
            return resource.getFile().getAbsolutePath();
        } catch (IOException e) {
            throw new InternalErrorException(e);
        }
    }

    // 상품 등록
    @Transactional
    public Long registerItem(RegisterRequestDto registerRequestDto, MultipartFile file) {
        String uploadFileName = fileManager.uploadFile(file);

        registerRequestDto.setImage(uploadFileName);

        Category dbCategory = categoryRepository.findByCategoryName(registerRequestDto.getCategory())
                .orElseThrow(() -> new BadRequestException(CategoryResponse.CATEGORY_NOT_EXISTS.getMessage()));

        Product savableProduct = itemRegisterMapper.toEntity(registerRequestDto);

        savableProduct.updateCategoryInfo(dbCategory);

        return productRepository.save(savableProduct).getId();
    }

    // 상품 수정
    @Transactional
    public void modifyItem(ModifyRequestDto modifyRequestDto, MultipartFile file) {
        Product dbProduct = productRepository.findById(modifyRequestDto.getId()).orElseThrow(() -> new BadRequestException(ProductResponse.ITEM_NOT_EXISTS.getMessage()));
        Product updatableProduct = itemModifyMapper.toEntity(modifyRequestDto);
        Category updatableCategory = categoryRepository.findByCategoryName(modifyRequestDto.getCategory())
                .orElseThrow(() -> new BadRequestException(CategoryResponse.CATEGORY_NOT_EXISTS.getMessage()));

        dbProduct.updateProductInfo(updatableProduct);
        dbProduct.updateCategoryInfo(updatableCategory);

        if (file != null) {
            String savedImageName = dbProduct.getImage();
            fileManager.deleteFile(savedImageName);

            String uploadFileName = fileManager.uploadFile(file);
            dbProduct.updateImageInfo(uploadFileName);
        }
    }

    @Transactional
    public void removeItem(Long productId) {
        Product dbProduct = productRepository.findById(productId).orElseThrow(() -> new BadRequestException(ProductResponse.ITEM_NOT_EXISTS.getMessage()));
        dbProduct.updateItemStatus(ChickenStatus.EXTINCTION);
    }

    public void validatePayAmount(Long itemNo, long totalPrice, int itemQuantity) {
        Product dbProduct = productRepository.findById(itemNo).orElseThrow(() -> new BadRequestException(ProductResponse.ITEM_NOT_EXISTS.getMessage()));

        if ((long) dbProduct.getPrice() * itemQuantity != totalPrice) {
            throw new BadRequestException(ProductResponse.INVALID_PAY_AMOUNT.getMessage());
        }
    }
}
