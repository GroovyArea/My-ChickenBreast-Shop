package com.daniel.mychickenbreastshop.product.item.adaptor.in.web.rest;

import com.daniel.mychickenbreastshop.document.utils.ControllerTest;
import com.daniel.mychickenbreastshop.product.category.domain.enums.ChickenCategory;
import com.daniel.mychickenbreastshop.product.item.application.port.item.in.ItemSearchUseCase;
import com.daniel.mychickenbreastshop.product.item.domain.enums.ChickenStatus;
import com.daniel.mychickenbreastshop.product.item.model.dto.request.ItemSearchDto;
import com.daniel.mychickenbreastshop.product.item.model.dto.response.DetailResponseDto;
import com.daniel.mychickenbreastshop.product.item.model.dto.response.ListResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.daniel.mychickenbreastshop.document.utils.RestDocsConfig.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductApiController.class)
class ProductApiControllerTest extends ControllerTest {

    @MockBean
    private ItemSearchUseCase searchUseCase;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentationContextProvider) {
        mockMvc =
                MockMvcBuilders.standaloneSetup(new ProductApiController(searchUseCase))
                        .addFilters(new CharacterEncodingFilter("UTF-8", true))
                        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                        .apply(documentationConfiguration(restDocumentationContextProvider))
                        .alwaysDo(MockMvcResultHandlers.print())
                        .alwaysDo(restDocs)
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

        // when
        ResultActions resultActions =
                mockMvc.perform(
                        get("/api/v1/products/{productId}", dto.getId()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(createJson(dto)))
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("productId").description("상품 id")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("상품 id"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("상품 이름"),
                                fieldWithPath("categoryName").type(JsonFieldType.VARIES).description("상품 카테고리 이름"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("상품 재고 수량"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("상품 정보 설명 컨텐츠"),
                                fieldWithPath("image").type(JsonFieldType.STRING).description("상품 이미지 다운로드 url")
                        )
                ));
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

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/products/category/{category}", category)
                        .param("page", String.valueOf(page)));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(createJson(pageOneItems)))
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("category").description("상품 카테고리 enum 값")
                        ),
                        requestParameters(
                                parameterWithName("page").optional().description("요청 페이지 번호")
                        ),
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("상품 id"),
                                fieldWithPath("[].name").type(JsonFieldType.STRING).description("상품 이름"),
                                fieldWithPath("[].price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                fieldWithPath("[].quantity").type(JsonFieldType.NUMBER).description("상품 재고 수량"),
                                fieldWithPath("[].image").type(JsonFieldType.STRING).description("상품 이미지 다운로드 url"),
                                fieldWithPath("[].category").type(JsonFieldType.VARIES).description("상품 카테고리 enum 값"),
                                fieldWithPath("[].status").type(JsonFieldType.VARIES).description("상품 상태 enum 값"),
                                fieldWithPath("[].createdAt").type(JsonFieldType.ARRAY).description("상품 등록 일시"),
                                fieldWithPath("[].updatedAt").type(JsonFieldType.ARRAY).description("상품 정보 수정 일시"),
                                fieldWithPath("[].deletedAt").type(JsonFieldType.ARRAY).description("상품 삭제 일시")
                        )
                ));
    }

    @DisplayName("API 요청을 통해 검색 조건에 맞는 상품 목록 10개를 페이징하여 반환한다.")
    @Test
    void getSearchProducts() throws Exception {
        // given
        List<ListResponseDto> pageOneSearchItems = getPageOneItems();
        ChickenCategory category = ChickenCategory.STEAMED;
        ChickenStatus status = ChickenStatus.SALE;

        ItemSearchDto dto = ItemSearchDto.builder()
                .searchKey("name")
                .searchValue("name")
                .status(status)
                .build();

        MultiValueMap<String, String> params = createParams(dto);

        given(searchUseCase.searchProducts(any(Pageable.class), any(ChickenCategory.class),
                any(ItemSearchDto.class)))
                .willReturn(pageOneSearchItems);

        // when
        ResultActions resultActions =
                mockMvc.perform(get("/api/v1/products/details/{category}", category)
                        .params(params));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(createJson(pageOneSearchItems)))
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("category").description("상품 카테고리 enum 값")
                        ),
                        requestParameters(
                                parameterWithName("page").optional().description("요청 페이지 번호"),
                                parameterWithName("status").description("상품 상태 enum 값"),
                                parameterWithName("searchKey").description("상품 검색 키 조건").attributes(field("constraints", "상품 번호, 상품 이름")),
                                parameterWithName("searchValue").description("상품 검색 값 조건")
                        ),
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("상품 id"),
                                fieldWithPath("[].name").type(JsonFieldType.STRING).description("상품 이름"),
                                fieldWithPath("[].price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                fieldWithPath("[].quantity").type(JsonFieldType.NUMBER).description("상품 재고 수량"),
                                fieldWithPath("[].image").type(JsonFieldType.STRING).description("상품 이미지 다운로드 url"),
                                fieldWithPath("[].category").type(JsonFieldType.VARIES).description("상품 카테고리 enum 값"),
                                fieldWithPath("[].status").type(JsonFieldType.VARIES).description("상품 상태 enum 값"),
                                fieldWithPath("[].createdAt").type(JsonFieldType.ARRAY).description("상품 등록 일시"),
                                fieldWithPath("[].updatedAt").type(JsonFieldType.ARRAY).description("상품 정보 수정 일시"),
                                fieldWithPath("[].deletedAt").type(JsonFieldType.ARRAY).description("상품 삭제 일시")
                        )
                ));
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