package com.daniel.mychickenbreastshop.product.item.adaptor.in.web.rest;

import com.daniel.mychickenbreastshop.global.util.JsonUtil;
import com.daniel.mychickenbreastshop.product.category.domain.enums.ChickenCategory;
import com.daniel.mychickenbreastshop.product.item.application.port.item.in.ItemSearchUseCase;
import com.daniel.mychickenbreastshop.product.item.domain.enums.ChickenStatus;
import com.daniel.mychickenbreastshop.product.item.model.dto.request.ItemSearchDto;
import com.daniel.mychickenbreastshop.product.item.model.dto.response.DetailResponseDto;
import com.daniel.mychickenbreastshop.product.item.model.dto.response.ListResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(ProductApiController.class)
class ProductApiControllerTest {

    @MockBean
    private ItemSearchUseCase searchUseCase;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentationContextProvider) {
        mockMvc =
                MockMvcBuilders.standaloneSetup(new ProductApiController(searchUseCase))
                        .addFilters(new CharacterEncodingFilter("UTF-8", true))
                        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                        .apply(documentationConfiguration(restDocumentationContextProvider))
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

        given(searchUseCase.getProduct(dto.getId())).willReturn(dto);

        // when & then
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

        given(searchUseCase.getAllProducts(any(ChickenCategory.class), any(Pageable.class)))
                .willReturn(pageOneItems);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", String.valueOf(page));

        // when & then
        mockMvc.perform(get("/api/v1/products/category/{category}", category)
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
        ChickenCategory category = ChickenCategory.STEAMED;
        ChickenStatus status = ChickenStatus.SALE;

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("status", status.name());
        params.add("searchKey", "name");
        params.add("searchValue", "name");

        given(searchUseCase.searchProducts(any(Pageable.class), any(ChickenStatus.class), any(ChickenCategory.class), any(ItemSearchDto.class)))
                .willReturn(pageOneSearchItems);

        // when & then
        mockMvc.perform(get("/api/v1/products/details/{category}", category)
                        .params(params))
                .andExpect(status().isOk())
                .andExpect(content().string(parseObject(pageOneSearchItems)))
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