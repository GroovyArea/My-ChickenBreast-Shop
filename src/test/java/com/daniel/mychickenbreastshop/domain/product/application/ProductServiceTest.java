package com.daniel.mychickenbreastshop.domain.product.application;

import com.daniel.mychickenbreastshop.ApplicationTest;
import com.daniel.mychickenbreastshop.domain.product.application.manage.FileManager;
import com.daniel.mychickenbreastshop.domain.product.model.category.Category;
import com.daniel.mychickenbreastshop.domain.product.model.category.enums.ChickenCategory;
import com.daniel.mychickenbreastshop.domain.product.model.item.Product;
import com.daniel.mychickenbreastshop.domain.product.model.item.dto.request.ItemSearchDto;
import com.daniel.mychickenbreastshop.domain.product.model.item.dto.request.ModifyRequestDto;
import com.daniel.mychickenbreastshop.domain.product.model.item.dto.request.RegisterRequestDto;
import com.daniel.mychickenbreastshop.domain.product.model.item.dto.response.DetailResponseDto;
import com.daniel.mychickenbreastshop.domain.product.model.item.dto.response.ListResponseDto;
import com.daniel.mychickenbreastshop.domain.product.model.item.enums.ChickenStatus;
import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductServiceTest extends ApplicationTest {

    @Value("${file.upload.location}")
    String uploadPath;

    @Autowired
    private ProductService productService;

    @Autowired
    private FileManager fileManager;

    @BeforeEach
    void setUpItems() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Category category = Category.builder()
                .id(1L)
                .categoryName(ChickenCategory.STEAMED)
                .products(new ArrayList<>())
                .build();

        categoryRepository.save(category);

        for (int i = 0; i < 50; i++) {
            String imageFileName = "image" + i + ".png";

            MockMultipartFile multipartFile = new MockMultipartFile(
                    "image", imageFileName,
                    MediaType.IMAGE_PNG_VALUE, "image".getBytes(StandardCharsets.UTF_8));

            String uploadFileName = fileManager.uploadFile(multipartFile);

            Product build = Product.builder()
                    .id((long) i)
                    .name("name" + i)
                    .price(i * 1000)
                    .quantity(i * 100)
                    .content("content" + i)
                    .image(uploadFileName)
                    .status(ChickenStatus.SALE)
                    .build();

            build.updateCategoryInfo(category);
            build.create();

            productRepository.save(build);
        }
    }

    @DisplayName("상품 상세 정보를 조회한다.")
    @Test
    void getProduct() {
        DetailResponseDto product = productService.getProduct(1L);

        assertThat(product.getName()).isEqualTo("name1");
        assertThat(product.getPrice()).isEqualTo(1000);
    }

    @DisplayName("전달 받은 카테고리에 해당하는 상품 목록을 반환한다.")
    @Test
    void getAllProduct() {
        List<ListResponseDto> products = productService.getAllProduct(ChickenCategory.STEAMED, 1);

        assertThat(products).hasSize(10);
        products.forEach(product -> assertThat(product.getCategory()).isEqualTo(ChickenCategory.STEAMED.getChickenName()));
    }

    @DisplayName("검색 조건에 맞는 상품 목록을 반환한다.")
    @Test
    void searchProducts() {
        // given
        ItemSearchDto itemSearchDto = new ItemSearchDto("name", "name");
        int page = 1;
        ChickenStatus status = ChickenStatus.SALE;
        ChickenCategory category = ChickenCategory.STEAMED;
        // when
        List<ListResponseDto> listResponseDtos = productService.searchProducts(page, status, category, itemSearchDto);

        assertThat(listResponseDtos).hasSize(10);
        listResponseDtos.forEach(listResponseDto -> assertThat(listResponseDto.getCategory()).isEqualTo(category.getChickenName()));
        listResponseDtos.forEach(listResponseDto -> assertThat(listResponseDto.getStatus()).isEqualTo(status.getStatusName()));
        listResponseDtos.forEach(listResponseDto -> assertThat(listResponseDto.getName()).contains("name"));
    }

    @DisplayName("이미지 파일의 리소스를 반환한다.")
    @Test
    void getItemImageResource() {
        // given
        Product product = productRepository.findById(1L).orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다."));
        String imageName = product.getImage();

        // when
        Resource itemImageResource = productService.getItemImageResource(imageName);

        assertThat(itemImageResource.getFilename()).isEqualTo(imageName);
    }

    @Test
    void getItemFilePath() {
        // given
        Product product = productRepository.findById(1L).orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다."));
        String imageName = product.getImage();
        Resource itemImageResource = productService.getItemImageResource(imageName);

        // when
        String itemFilePath = productService.getItemFilePath(itemImageResource);

        assertThat(itemFilePath).startsWith(uploadPath);
    }

    @DisplayName("상품을 등록한다.")
    @Test
    void registerItem() {
        // given
        RegisterRequestDto requestDto = RegisterRequestDto.builder()
                .name("name")
                .price(1000).quantity(200)
                .content("content")
                .status(ChickenStatus.SALE)
                .category(ChickenCategory.STEAMED)
                .build();

        String imageFileName = "image.png";

        MockMultipartFile multipartFile = new MockMultipartFile(
                "image", imageFileName,
                MediaType.IMAGE_PNG_VALUE, "image".getBytes(StandardCharsets.UTF_8));

        // when
        Long id = productService.registerItem(requestDto, multipartFile);

        assertThat(id).isNotNull();
    }

    @DisplayName("상품 정보를 수정한다.")
    @Test
    void modifyItem() {
        // given
        ModifyRequestDto modifyRequestDto = ModifyRequestDto.builder()
                .id(1L)
                .name("name")
                .price(1000).quantity(200)
                .content("content")
                .status(ChickenStatus.SALE)
                .category(ChickenCategory.STEAMED)
                .build();

        String imageFileName = "modifiedImage.png";

        MockMultipartFile multipartFile = new MockMultipartFile(
                "image", imageFileName,
                MediaType.IMAGE_PNG_VALUE, "image".getBytes(StandardCharsets.UTF_8));

        // when
        productService.modifyItem(modifyRequestDto, multipartFile);
        Product product = productRepository.findById(1L).orElseThrow(() -> new RuntimeException("해당 상품이 존재하지 않음"));

        assertThat(product.getContent()).isEqualTo("content");
        assertThat(product.getName()).isEqualTo("name");
        assertThat(product.getImage()).contains("modifiedImage.png");
    }

    @DisplayName("상품을 단종 상태로 변경한다.")
    @Test
    void removeItem() {
        productService.removeItem(1L);
        Product product = productRepository.findById(1L).orElseThrow(() -> new RuntimeException("해당 상품이 존재하지 않음"));

        assertThat(product.getStatus()).isEqualTo(ChickenStatus.EXTINCTION);
    }

    @DisplayName("결제할 상품 가격과 수량의 총 결제 금액의 유효성을 검사하며 불일치할 경우 예외를 발생시킨다.")
    @Test
    void validatePayAmount() {
        // given
        Long itemNo = 1L;
        int itemQuantity = 10;
        long totalPrice = 1000; // 원래 10,000원 이어야 함.

        assertThatThrownBy(() -> productService.validatePayAmount(itemNo, itemQuantity, totalPrice))
                .isInstanceOf(BadRequestException.class).hasMessageContaining("상품 총 가격이 잘못 되었습니다.");
    }
}