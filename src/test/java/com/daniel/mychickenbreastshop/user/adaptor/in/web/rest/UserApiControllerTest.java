package com.daniel.mychickenbreastshop.user.adaptor.in.web.rest;

import com.daniel.mychickenbreastshop.WithAuthUser;
import com.daniel.mychickenbreastshop.document.utils.ControllerTest;
import com.daniel.mychickenbreastshop.global.util.JsonUtil;
import com.daniel.mychickenbreastshop.user.application.port.in.ManageUserUseCase;
import com.daniel.mychickenbreastshop.user.application.port.in.UserSearchUseCase;
import com.daniel.mychickenbreastshop.user.domain.enums.Role;
import com.daniel.mychickenbreastshop.user.model.dto.request.ModifyRequestDto;
import com.daniel.mychickenbreastshop.user.model.dto.response.DetailResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserApiController.class)
class UserApiControllerTest extends ControllerTest {

    @MockBean
    private UserSearchUseCase userSearchService;

    @MockBean
    private ManageUserUseCase manageUserService;

    @BeforeEach
    void setUp(final RestDocumentationContextProvider provider) {
        this.mockMvc =
                MockMvcBuilders.standaloneSetup(
                                new UserApiController(manageUserService, userSearchService))
                        .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                        .alwaysDo(MockMvcResultHandlers.print())
                        .alwaysDo(restDocs)
                        .addFilters(new CharacterEncodingFilter(StandardCharsets.UTF_8.displayName(), true))
                        .build();
    }

    @DisplayName("API ????????? ?????? ?????? ????????? ????????????.")
    @Test
    void getUserDetail() throws Exception {
        // given
        DetailResponseDto dto = DetailResponseDto.builder()
                .userId(1L)
                .loginId("loginId")
                .name("name")
                .email("0909@gmail.com")
                .address("????????? ????????? ?????????")
                .zipcode("123-123")
                .role(Role.ROLE_USER)
                .build();

        given(userSearchService.getUser(anyLong())).willReturn(dto);

        // when
        ResultActions resultActions =
                mockMvc.perform(
                        get("/api/v1/users/{userId}", dto.getUserId()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(createJson(dto)))
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("userId").description("?????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                fieldWithPath("loginId").type(JsonFieldType.STRING).description("????????? ?????????"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("address").type(JsonFieldType.STRING).description("??????"),
                                fieldWithPath("zipcode").type(JsonFieldType.STRING).description("????????????"),
                                fieldWithPath("role").type(JsonFieldType.VARIES).description("?????? ??????")
                        )
                ));
    }

    @WithAuthUser
    @DisplayName("API ????????? ?????? ?????? ????????? ????????????.")
    @Test
    void modifyUser() throws Exception {
        // given
        ModifyRequestDto dto = ModifyRequestDto.builder()
                .name("name")
                .password("update password")
                .email("234@gmail.com")
                .address("????????? ????????? ?????????")
                .zipcode("123-123")
                .build();

        // when
        ResultActions resultActions =
                mockMvc.perform(patch("/api/v1/users")
                        .content(JsonUtil.objectToString(dto))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("????????? ??????"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("????????? ????????????"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("????????? ?????????"),
                                fieldWithPath("address").type(JsonFieldType.STRING).description("????????? ??????"),
                                fieldWithPath("zipcode").type(JsonFieldType.STRING).description("????????? ?????? ??????")
                        )
                ));
    }
}