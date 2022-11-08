package com.daniel.mychickenbreastshop.domain.user.api;

import com.daniel.mychickenbreastshop.domain.user.application.MailService;
import com.daniel.mychickenbreastshop.domain.user.application.UserService;
import com.daniel.mychickenbreastshop.domain.user.model.dto.request.EmailRequestDto;
import com.daniel.mychickenbreastshop.domain.user.model.dto.request.JoinRequestDto;
import com.daniel.mychickenbreastshop.global.util.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(JoinApiController.class)
@AutoConfigureRestDocs
class JoinApiControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private MailService mailService;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentationContextProvider) {
        mockMvc =
                MockMvcBuilders.standaloneSetup(new JoinApiController(userService, mailService))
                        .apply(documentationConfiguration(restDocumentationContextProvider))
                        .alwaysDo(document("{method-name}", preprocessRequest(prettyPrint())))
                        .build();
    }

    @DisplayName("회원가입을 진행한다.")
    @Test
    void join() throws Exception {
        // given
        JoinRequestDto dto = JoinRequestDto.builder()
                .loginId("loginId")
                .name("name")
                .email("0909asdf@gmail.com")
                .emailAuthKey("123456")
                .address("서울시 서초구 서초동")
                .zipcode("123-123")
                .build();

        given(userService.join(any(JoinRequestDto.class))).willReturn(1L);

        mockMvc.perform(post("/join")
                        .content(parseObject(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("1"))
                .andDo(print());
    }

    @DisplayName("회원 가입 이메일로 인증 번호를 전송한다.")
    @Test
    void sendEmailKey() throws Exception {
        // given
        EmailRequestDto dto = new EmailRequestDto("asdf@gmail.com");

        mockMvc.perform(post("/join/email")
                        .content(parseObject(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    String parseObject(Object object) {
        return JsonUtil.objectToString(object);
    }
}