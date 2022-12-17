package com.daniel.mychickenbreastshop.product.item.application.service;

import com.daniel.mychickenbreastshop.product.category.adaptor.out.persistence.CategoryRepository;
import com.daniel.mychickenbreastshop.product.category.domain.Category;
import com.daniel.mychickenbreastshop.product.category.domain.enums.ChickenCategory;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ManageItemServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private FileStore s3FileStore;

    @Mock
    private ItemRegisterMapper itemRegisterMapper;

    @Mock
    private ItemModifyMapper itemModifyMapper;

    @InjectMocks
    private ManageItemUseCase manageItemService;


    private List<Product> products;

    private Category category;

    @BeforeEach
    void setUpItems() {
        products = new ArrayList<>();

        category = Category.builder()
                .id(1L)
                .categoryName(ChickenCategory.STEAMED)
                .products(products)
                .build();

        for (int i = 1; i <= 50; i++) {
            Product product = Product.builder()
                    .id((long) i)
                    .name("name" + i)
                    .price(i * 1000)
                    .quantity(i * 100)
                    .image("image" + i)
                    .content("content" + i)
                    .status(ChickenStatus.SALE)
                    .build();

            product.updateCategoryInfo(category);

            products.add(product);


        }
    }

    @DisplayName("상품을 등록한다.")
    @Test
    void registerItem() {
        // given
        String imageFileName = "image.png";

        MockMultipartFile multipartFile = new MockMultipartFile("image", imageFileName,
                MediaType.IMAGE_PNG_VALUE, "image".getBytes(StandardCharsets.UTF_8));

        FileResponse fileResponse = FileResponse.builder()
                .downloadFileResource(new byte[100])
                .uploadFileUrl("upload_file_url")
                .build();

        RegisterRequestDto registerRequestDto = RegisterRequestDto.builder()
                .name("name")
                .price(1000).quantity(200)
                .status(ChickenStatus.SALE)
                .category(ChickenCategory.STEAMED)
                .build();

        Category category = Category.builder()
                .categoryName(ChickenCategory.STEAMED)
                .build();

        // when
        when(s3FileStore.upload(multipartFile)).thenReturn(fileResponse);
        when(categoryRepository.findByCategoryName(registerRequestDto.getCategory()))
                .thenReturn(Optional.ofNullable(category));
        when(itemRegisterMapper.toEntity(registerRequestDto)).thenReturn(products.get(0));
        when(productRepository.save(products.get(0))).thenReturn(products.get(0));

        Long itemId = manageItemService.registerItem(registerRequestDto, multipartFile);

        assertThat(itemId).isEqualTo(1L);
    }

    @DisplayName("상품 정보를 수정한다.")
    @Test
    void modifyItem() {
        // given
        ModifyRequestDto modifyRequestDto = ModifyRequestDto.builder()
                .id(1L)
                .name("change_name")
                .price(1000).quantity(200)
                .content("change_content")
                .status(ChickenStatus.SALE)
                .category(ChickenCategory.STEAMED)
                .build();

        Product original = Product.builder()
                .id(1L)
                .name("name")
                .price(3000).quantity(200)
                .content("content")
                .status(ChickenStatus.SALE)
                .category(category)
                .build();

        Product modified = Product.builder()
                .id(1L)
                .name("change_name")
                .price(1000).quantity(200)
                .content("change_content")
                .status(ChickenStatus.SALE)
                .category(category)
                .build();

        when(categoryRepository.findByCategoryName(modifyRequestDto.getCategory()))
                .thenReturn(Optional.ofNullable(category));
        when(productRepository.findById(modifyRequestDto.getId())).thenReturn(Optional.ofNullable(original));
        when(itemModifyMapper.toEntity(modifyRequestDto)).thenReturn(modified);

        manageItemService.modifyItem(modifyRequestDto);

        assertThat(original.getName()).isEqualTo(modified.getName());
        assertThat(original.getPrice()).isEqualTo(modified.getPrice());
        assertThat(original.getContent()).isEqualTo(modified.getContent());
    }

    @DisplayName("상품을 단종 상품으로 변경한다.")
    @Test
    void removeItem() {
        // given
        long itemId = products.get(0).getId();

        // when
        when(productRepository.findById(itemId)).thenReturn(Optional.ofNullable(products.get(0)));

        manageItemService.removeItem(itemId);

        assertThat(products.get(0).getStatus()).isEqualTo(ChickenStatus.EXTINCTION);
    }

    @DisplayName("상품 이미지 파일을 변경한다.")
    @Test
    void changeImage() {
        // given
        Product original = Product.builder()
                .id(1L)
                .image("originalImage.jpg")
                .build();

        String imageFileName = "modifiedImage.png";
        String uploadFileName = "uploadFileName.png";

        MockMultipartFile multipartFile = new MockMultipartFile(
                "image", imageFileName,
                MediaType.IMAGE_PNG_VALUE, "image".getBytes(StandardCharsets.UTF_8));

        FileResponse fileResponse = FileResponse.builder()
                .uploadFileUrl("upload_file_url")
                .build();

        // when
        when(s3FileStore.upload(multipartFile)).thenReturn(fileResponse);
        when(productRepository.findById(original.getId())).thenReturn(Optional.ofNullable(original));

        manageItemService.changeImage(original.getId(), multipartFile);

        assertThat(original.getImage()).isEqualTo(uploadFileName);

    }
}