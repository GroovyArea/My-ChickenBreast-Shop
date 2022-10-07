package com.daniel.mychickenbreastshop.domain.product.application;

import com.daniel.mychickenbreastshop.ApplicationTest;
import com.daniel.mychickenbreastshop.domain.product.application.manage.FileManager;
import com.daniel.mychickenbreastshop.domain.product.domain.category.Category;
import com.daniel.mychickenbreastshop.domain.product.domain.category.model.ChickenCategory;
import com.daniel.mychickenbreastshop.domain.product.domain.item.Product;
import com.daniel.mychickenbreastshop.domain.product.domain.item.dto.request.ItemSearchDto;
import com.daniel.mychickenbreastshop.domain.product.domain.item.dto.response.DetailResponseDto;
import com.daniel.mychickenbreastshop.domain.product.domain.item.dto.response.ListResponseDto;
import com.daniel.mychickenbreastshop.domain.product.domain.item.model.ChickenStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ProductServiceTest extends ApplicationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileManager fileManager;

    @Autowired
    ResourceLoader loader;

    @BeforeEach
    void setUpItems() throws IOException {
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
            String uploadFileName = UUID.randomUUID() + "_" + imageFileName;

            MockMultipartFile multipartFile = new MockMultipartFile("image" + i, "image" + i + ".png",
                    MediaType.IMAGE_PNG_VALUE, ("image" + i).getBytes(StandardCharsets.UTF_8));

            fileManager.uploadFile(multipartFile);

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
    }

    @Test
    void registerItem() {
    }

    @Test
    void modifyItem() {
    }

    @Test
    void removeItem() {
    }

    @Test
    void validatePayAmount() {
    }
}