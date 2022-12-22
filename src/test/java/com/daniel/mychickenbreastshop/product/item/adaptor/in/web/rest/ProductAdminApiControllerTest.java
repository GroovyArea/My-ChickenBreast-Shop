package com.daniel.mychickenbreastshop.product.item.adaptor.in.web.rest;

import com.daniel.mychickenbreastshop.global.util.JsonUtil;
import com.daniel.mychickenbreastshop.product.category.domain.enums.ChickenCategory;
import com.daniel.mychickenbreastshop.product.item.application.port.item.in.ManageItemUseCase;
import com.daniel.mychickenbreastshop.product.item.domain.enums.ChickenStatus;
import com.daniel.mychickenbreastshop.product.item.model.dto.request.RegisterRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
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
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(ProductAdminApiController.class)
class ProductAdminApiControllerTest {

    @MockBean
    private ManageItemUseCase manageItemService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentationContextProvider) {
        mockMvc =
                MockMvcBuilders.standaloneSetup(new ProductAdminApiController(manageItemService))
                        .addFilters(new CharacterEncodingFilter("UTF-8", true))
                        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                        .apply(documentationConfiguration(restDocumentationContextProvider))
                        .build();
    }

    @DisplayName("API 호출을 통해 상품을 등록한다.")
    @Test
    void registerProduct() throws Exception {
        //given
        RegisterRequestDto registerRequestDto = RegisterRequestDto.builder()
                .name("product_name")
                .price(10000)
                .quantity(1000)
                .content("product_content")
                .image("product_image.jpg")
                .status(ChickenStatus.SALE)
                .category(ChickenCategory.STEAMED)
                .build();

        String imageFileName = "product_image.jpg";
        MockMultipartFile imageFile = new MockMultipartFile("image", imageFileName,
                MediaType.IMAGE_PNG_VALUE, "image".getBytes(StandardCharsets.UTF_8));

        MockMultipartFile requestDto = new MockMultipartFile("registerRequestDto", "registerRequestDto",
                MediaType.APPLICATION_JSON_VALUE, parseObject(registerRequestDto).getBytes());

        long registeredProductId = 1;

        given(manageItemService.registerItem(any(RegisterRequestDto.class), any(MultipartFile.class)))
                .willReturn(registeredProductId);

        mockMvc.perform(multipart("/api/v2/products")
                        .file(imageFile)
                        .file(requestDto))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(registeredProductId)))
                .andDo(print());
    }

    private String parseObject(Object object) {
        return JsonUtil.objectToString(object);
    }
}