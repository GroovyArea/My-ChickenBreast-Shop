package com.daniel.mychickenbreastshop.domain.product.api.item;

import com.daniel.mychickenbreastshop.domain.product.application.ProductService;
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
import org.springframework.web.filter.CharacterEncodingFilter;

import java.net.MalformedURLException;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@WebMvcTest(ProductApiController.class)
class FileApiControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @BeforeEach
    void setUp() {
        mockMvc =
                MockMvcBuilders.standaloneSetup(new FileApiController(productService))
                        .addFilters(new CharacterEncodingFilter("UTF-8", true))
                        .build();
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
    void modifyFileOfItem() {
    }
}