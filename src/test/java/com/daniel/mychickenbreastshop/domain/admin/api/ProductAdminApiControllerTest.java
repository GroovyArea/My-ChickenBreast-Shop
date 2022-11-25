package com.daniel.mychickenbreastshop.domain.admin.api;

import com.daniel.mychickenbreastshop.domain.product.category.model.enums.ChickenCategory;
import com.daniel.mychickenbreastshop.domain.product.item.application.ProductService;
import com.daniel.mychickenbreastshop.domain.product.item.model.dto.request.ModifyRequestDto;
import com.daniel.mychickenbreastshop.domain.product.item.model.dto.request.RegisterRequestDto;
import com.daniel.mychickenbreastshop.domain.product.item.model.enums.ChickenStatus;
import com.daniel.mychickenbreastshop.global.util.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(ProductAdminApiController.class)
@AutoConfigureRestDocs
class ProductAdminApiControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentationContextProvider) {
        mockMvc =
                MockMvcBuilders.standaloneSetup(new ProductAdminApiController(productService))
                        .addFilters(new CharacterEncodingFilter("UTF-8", true))
                        .apply(documentationConfiguration(restDocumentationContextProvider))

                        .alwaysDo(document("{method-name}", preprocessRequest(prettyPrint())))
                        .build();
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

}