package com.daniel.mychickenbreastshop.domain.product.application;

import com.daniel.mychickenbreastshop.domain.product.application.manage.FileManager;
import com.daniel.mychickenbreastshop.domain.product.domain.category.Category;
import com.daniel.mychickenbreastshop.domain.product.domain.category.CategoryRepository;
import com.daniel.mychickenbreastshop.domain.product.domain.category.model.CategoryResponse;
import com.daniel.mychickenbreastshop.domain.product.domain.category.model.ChickenCategory;
import com.daniel.mychickenbreastshop.domain.product.domain.item.Product;
import com.daniel.mychickenbreastshop.domain.product.domain.item.ProductRepository;
import com.daniel.mychickenbreastshop.domain.product.domain.item.dto.request.ItemSearchDto;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final FileManager fileManager;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ItemDetailMapper itemDetailMapper;
    private final ItemListMapper itemListMapper;
    private final ItemRegisterMapper itemRegisterMapper;
    private final ItemModifyMapper itemModifyMapper;

    // 상품 단건 조회
    public DetailResponseDto getProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException(ProductResponse.ITEM_NOT_EXISTS.getMessage()));

        String downLoadURI = fileManager.getDownloadURI(product.getImage());

        DetailResponseDto dto = itemDetailMapper.toDTO(product);

        dto.updateImageUrl(downLoadURI);

        return dto;
    }

    // 상품 리스트 조회
    public List<ListResponseDto> getAllProduct(ChickenCategory category, int page) {
        PageRequest pageRequest = createPageRequest(page);
        List<Product> products = productRepository.findAllByCategoryName(category, pageRequest).getContent();

        return products.stream()
                .map(product -> {
                    ListResponseDto listResponseDto = itemListMapper.toDTO(product);
                    listResponseDto.changeStatusNameWithChickenStatus(product.getStatus().getStatusName());
                    listResponseDto.changeCategoryNameWithChickenCategory(product.getCategory().getCategoryName().getChickenName());
                    return listResponseDto;
                })
                .toList();
    }

    public List<ListResponseDto> searchProducts(int page, ChickenStatus status, ChickenCategory category, ItemSearchDto searchDto) {
        PageRequest pageRequest = createPageRequest(page);
        List<Product> searchedItems = productRepository.findItemWithDynamicQuery(pageRequest, searchDto, category, status).getContent();

        return searchedItems.stream()
                .map(product -> {
                    ListResponseDto listResponseDto = itemListMapper.toDTO(product);
                    listResponseDto.changeStatusNameWithChickenStatus(product.getStatus().getStatusName());
                    listResponseDto.changeCategoryNameWithChickenCategory(product.getCategory().getCategoryName().getChickenName());
                    return listResponseDto;
                })
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

        registerRequestDto.updateImageName(uploadFileName);

        Category dbCategory = categoryRepository.findByCategoryName(registerRequestDto.getCategory()).orElseThrow(() -> new BadRequestException(CategoryResponse.CATEGORY_NOT_EXISTS.getMessage()));

        Product savableProduct = itemRegisterMapper.toEntity(registerRequestDto);

        savableProduct.updateCategoryInfo(dbCategory);
        savableProduct.updateItemStatus(ChickenStatus.SALE);

        return productRepository.save(savableProduct).getId();
    }

    // 상품 수정
    @Transactional
    public void modifyItem(ModifyRequestDto modifyRequestDto, MultipartFile file) {
        Product dbProduct = productRepository.findById(modifyRequestDto.getId()).orElseThrow(() -> new BadRequestException(ProductResponse.ITEM_NOT_EXISTS.getMessage()));
        Product updatableProduct = itemModifyMapper.toEntity(modifyRequestDto);
        Category updatableCategory = categoryRepository.findByCategoryName(modifyRequestDto.getCategory()).orElseThrow(() -> new BadRequestException(CategoryResponse.CATEGORY_NOT_EXISTS.getMessage()));

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
        dbProduct.delete();
    }

    public void validatePayAmount(Long itemNo, int itemQuantity, long totalPrice) {
        Product dbProduct = productRepository.findById(itemNo).orElseThrow(() -> new BadRequestException(ProductResponse.ITEM_NOT_EXISTS.getMessage()));

        if ((long) dbProduct.getPrice() * itemQuantity != totalPrice) {
            throw new BadRequestException(ProductResponse.INVALID_PAY_AMOUNT.getMessage());
        }
    }

    private PageRequest createPageRequest(int page) {
        return PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

}
