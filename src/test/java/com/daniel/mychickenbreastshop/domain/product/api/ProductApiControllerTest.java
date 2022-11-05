package com.daniel.mychickenbreastshop.domain.product.api;

import com.daniel.mychickenbreastshop.domain.product.application.ProductService;
import com.daniel.mychickenbreastshop.domain.product.model.category.enums.ChickenCategory;
import com.daniel.mychickenbreastshop.domain.product.model.item.dto.request.ItemSearchDto;
import com.daniel.mychickenbreastshop.domain.product.model.item.dto.response.DetailResponseDto;
import com.daniel.mychickenbreastshop.domain.product.model.item.dto.response.ListResponseDto;
import com.daniel.mychickenbreastshop.domain.product.model.item.enums.ChickenStatus;
import com.daniel.mychickenbreastshop.global.util.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductApiController.class)
class ProductApiControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @BeforeEach
    void setUp() {
        mockMvc =
                MockMvcBuilders.standaloneSetup(new ProductApiController(productService))
                        .addFilters(new CharacterEncodingFilter("UTF-8", true))
                        .build();
    }

    @DisplayName("API 요청을 통해 상품 정보를 반환한다.")
    @Test
    void getProductDetail() throws Exception {
        // given
        DetailResponseDto dto = DetailResponseDto.builder()
                .id(1)
                .name("name")
                .categoryName(ChickenCategory.STEAMED)
                .price(10000)
                .quantity(100)
                .content("content")
                .image("image_download_url")
                .build();

        given(productService.getProduct(dto.getId())).willReturn(dto);

        mockMvc.perform(get("/api/v1/products/{productId}", dto.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(parseObject(dto)))
                .andDo(print());
    }

    @DisplayName("API 요청을 통해 상품 목록 10개를 페이징하여 반환한다.")
    @Test
    void getProducts() throws Exception {
        // given
        List<ListResponseDto> pageOneItems = getPageOneItems();
        int page = 1;
        ChickenCategory category = ChickenCategory.STEAMED;

        given(productService.getAllProduct(category, page)).willReturn(pageOneItems);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", String.valueOf(page));
        params.add("category", category.name());

        mockMvc.perform(get("/api/v1/products/category")
                        .params(params))
                .andExpect(status().isOk())
                .andExpect(content().string(parseObject(pageOneItems)))
                .andDo(print());
    }

    @DisplayName("API 요청을 통해 검색 조건에 맞는 상품 목록 10개를 페이징하여 반환한다.")
    @Test
    void getSearchProducts() throws Exception {
        // given
        List<ListResponseDto> pageOneSearchItems = getPageOneItems();
        int page = 1;
        ChickenCategory category = ChickenCategory.STEAMED;
        ChickenStatus status = ChickenStatus.SALE;

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", String.valueOf(page));
        params.add("status", status.name());
        params.add("searchKey", "name");
        params.add("searchValue", "name");

        given(productService.searchProducts(anyInt(), any(ChickenStatus.class), any(ChickenCategory.class), any(ItemSearchDto.class)))
                .willReturn(pageOneSearchItems);

        mockMvc.perform(get("/api/v2/products/search/{category}", category)
                        .params(params))
                .andExpect(status().isOk())
                .andExpect(content().string(parseObject(pageOneSearchItems)))
                .andDo(print());
    }

    @DisplayName("API 요청을 통해 상품 이미지 파일을 다운로드한다.")
    @Test
    void getDownloadFile() throws MalformedURLException {
        // given
        String fileName = "fileName";
        Resource resource = new UrlResource(URI.create("/test_file_path"));
        String imageFilePath = "test_image_file_path";
        MockHttpServletRequest request = new MockHttpServletRequest();

        given(productService.getItemImageResource(fileName)).willReturn(resource);
        given(productService.getItemFilePath(resource)).willReturn(imageFilePath);

    }

    @Test
    void registerProduct() {
    }

    @Test
    void modifyProduct() {
    }

    @Test
    void removeProduct() {
    }

    private String parseObject(Object object) {
        return JsonUtil.objectToString(object);
    }

    private List<ListResponseDto> getPageOneItems() {
        List<ListResponseDto> items = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            ListResponseDto dto = ListResponseDto.builder()
                    .id(i)
                    .name("item_name" + i)
                    .price(10000 * i)
                    .quantity(100)
                    .image("image_download_url" + i)
                    .category(ChickenCategory.STEAMED)
                    .status(ChickenStatus.SALE)
                    .updatedAt(LocalDateTime.now())
                    .createdAt(LocalDateTime.now())
                    .build();

            items.add(dto);
        }
        return items;
    }
}