package com.daniel.mychickenbreastshop.user.adaptor.in.web.rest;

import com.daniel.mychickenbreastshop.document.utils.ControllerTest;
import com.daniel.mychickenbreastshop.global.util.PasswordEncrypt;
import com.daniel.mychickenbreastshop.user.application.port.in.JoinUseCase;
import com.daniel.mychickenbreastshop.user.application.port.in.SendMailUseCase;
import com.daniel.mychickenbreastshop.user.domain.enums.Role;
import com.daniel.mychickenbreastshop.user.model.dto.request.EmailRequestDto;
import com.daniel.mychickenbreastshop.user.model.dto.request.JoinRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JoinApiController.class)
class JoinApiControllerTest extends ControllerTest {

    @MockBean
    private JoinUseCase joinUseService;

    @MockBean
    private SendMailUseCase sendMailService;

    @BeforeEach
    void setUp(RestDocumentationContextProvider provider) {
        mockMvc =
                MockMvcBuilders.standaloneSetup(new JoinApiController(joinUseService, sendMailService))
                        .apply(documentationConfiguration(provider))
                        .alwaysDo(MockMvcResultHandlers.print())
                        .alwaysDo(restDocs)
                        .build();
    }

    @DisplayName("회원가입을 진행한다.")
    @Test
    void join() throws Exception {
        // given
        JoinRequestDto dto = JoinRequestDto.builder()
                .loginId("loginId")
                .password("password")
                .salt(PasswordEncrypt.getSalt())
                .name("name")
                .email("0909asdf@gmail.com")
                .emailAuthKey("123456")
                .address("서울시 서초구 서초동")
                .zipcode("123-123")
                .role(Role.ROLE_USER)
                .build();

        given(joinUseService.join(any(JoinRequestDto.class))).willReturn(1L);

        // when
        ResultActions resultActions =
                mockMvc.perform(post("/join")
                        .content(createJson(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string("1"))
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 아이디"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("로그인 비밀번호"),
                                fieldWithPath("salt").type(JsonFieldType.STRING).description("로그인 비밀번호 암호 첨가물"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("회원 이름"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("회원 이메일"),
                                fieldWithPath("emailAuthKey").type(JsonFieldType.STRING).description("이메일 인증 번호"),
                                fieldWithPath("address").type(JsonFieldType.STRING).description("주소"),
                                fieldWithPath("zipcode").type(JsonFieldType.STRING).description("우편번호"),
                                fieldWithPath("role").type(JsonFieldType.VARIES).description("회원 등급")
                        )
                ));
    }

    @DisplayName("회원 가입 이메일로 인증 번호를 전송한다.")
    @Test
    void sendEmailKey() throws Exception {
        // given
        EmailRequestDto dto = new EmailRequestDto("asdf@gmail.com");

        // when
        ResultActions resultActions = mockMvc.perform(post("/join/email")
                .content(createJson(dto))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("회원 가입 이메일")
                        )
                ));
    }
}