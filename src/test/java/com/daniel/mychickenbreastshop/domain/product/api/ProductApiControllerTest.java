package com.daniel.mychickenbreastshop.domain.product.api;

import com.daniel.mychickenbreastshop.domain.product.api.item.ProductApiController;
import com.daniel.mychickenbreastshop.domain.product.application.ProductService;
import com.daniel.mychickenbreastshop.domain.product.model.category.enums.ChickenCategory;
import com.daniel.mychickenbreastshop.domain.product.model.item.dto.request.ItemSearchDto;
import com.daniel.mychickenbreastshop.domain.product.model.item.dto.request.ModifyRequestDto;
import com.daniel.mychickenbreastshop.domain.product.model.item.dto.request.RegisterRequestDto;
import com.daniel.mychickenbreastshop.domain.product.model.item.dto.response.DetailResponseDto;
import com.daniel.mychickenbreastshop.domain.product.model.item.dto.response.ListResponseDto;
import com.daniel.mychickenbreastshop.domain.product.model.item.enums.ChickenStatus;
import com.daniel.mychickenbreastshop.global.util.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

        // when & then
        mockMvc.perform(get("/api/v2/products/search/{category}", category)
                        .params(params))
                .andExpect(status().isOk())
                .andExpect(content().string(parseObject(pageOneSearchItems)))
                .andDo(print());
    }

    @DisplayName("API 요청을 통해 상품 정보를 서버에 등록한다.")
    @Test
    void registerProduct() throws Exception {
        // given
        RegisterRequestDto dto = RegisterRequestDto.builder()
                .name("name")
                .price(10000)
                .quantity(100)
                .content("content")
                .status(ChickenStatus.SALE)
                .category(ChickenCategory.STEAMED)
                .build();

        String parsedObject = parseObject(dto);
        MockMultipartFile dtoFile = new MockMultipartFile("registerRequestDto", "registerRequestDto", MediaType.APPLICATION_JSON_VALUE, parsedObject.getBytes(StandardCharsets.UTF_8));

        String imageFileName = "image.png";
        MockMultipartFile imageFile = new MockMultipartFile("image", imageFileName,
                MediaType.IMAGE_PNG_VALUE, "image".getBytes(StandardCharsets.UTF_8));

        Long registeredItemId = 1L;

        given(productService.registerItem(any(RegisterRequestDto.class), any(MultipartFile.class))).willReturn(registeredItemId);

        // when & then
        mockMvc.perform(multipart("/api/v2/products")
                        .file(dtoFile)
                        .file(imageFile))
                .andExpect(status().isOk())
                .andExpect(content().string("1"))
                .andDo(print());
    }

    @DisplayName("API 요청을 통해 상품 정보를 수정한다.")
    @Test
    void modifyProduct() throws Exception {
        // given
        ModifyRequestDto dto = ModifyRequestDto.builder()
                .id(1L)
                .name("name")
                .price(20000)
                .quantity(200)
                .content("content")
                .status(ChickenStatus.SALE)
                .category(ChickenCategory.STEAMED)
                .build();

        // when & then
        mockMvc.perform(patch("/api/v2/products")
                        .content(parseObject(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("API 요청을 통해 상품 상태를 단종 상태로 변경한다.")
    @Test
    void removeProduct() throws Exception {
        // given
        Long productId = 1L;

        mockMvc.perform(delete("/api/v2/products/{productId}", productId))
                .andExpect(status().isOk())
                .andDo(print());
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