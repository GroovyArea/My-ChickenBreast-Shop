package com.daniel.mychickenbreastshop.product.item.adaptor.in.web.rest;

import com.daniel.mychickenbreastshop.product.item.application.port.file.in.GetFileResourceUseCase;
import com.daniel.mychickenbreastshop.product.item.application.port.item.in.ManageItemUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(FileApiController.class)
class FileApiControllerTest {

    @MockBean
    private  ManageItemUseCase itemUseCase;

    @MockBean
    private  GetFileResourceUseCase fileResourceUseCase;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentationContextProvider) {
        mockMvc =
                MockMvcBuilders.standaloneSetup(new FileApiController(itemUseCase, fileResourceUseCase))
                        .apply(documentationConfiguration(restDocumentationContextProvider))
                        .alwaysDo(document("{method-name}", preprocessRequest(prettyPrint())))
                        .build();
    }

    @DisplayName("API 요청을 통해 상품 이미지 파일을 다운로드한다.")
    @Test
    void getDownloadFile() throws Exception {
        // given
        String fileName = "fileName.jpg";
        byte[] data = new byte[0];

        given(fileResourceUseCase.getFileOfByteResource(fileName)).willReturn(data);

        mockMvc.perform(get("/api/v2/files/download/{fileName}", fileName))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(header().exists(HttpHeaders.CONTENT_DISPOSITION))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("API 요청을 통해 상품의 이미지 파일을 변경한다.")
    @Test
    void modifyFileOfItem() throws Exception {
        // given
        String imageFileName = "change_image.png";
        MockMultipartFile imageFile = new MockMultipartFile("image", imageFileName,
                MediaType.IMAGE_PNG_VALUE, "image".getBytes(StandardCharsets.UTF_8));

        Long productId = 1L;

        // when & then
        mockMvc.perform(multipart("/api/v2/files/{id}", productId)
                        .file(imageFile))
                .andExpect(status().isOk())
                .andDo(print());
    }
}