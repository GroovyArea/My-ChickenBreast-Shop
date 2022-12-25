package com.daniel.mychickenbreastshop.user.adaptor.in.web.rest;

import com.daniel.mychickenbreastshop.WithAuthUser;
import com.daniel.mychickenbreastshop.document.utils.ControllerTest;
import com.daniel.mychickenbreastshop.user.application.port.in.ManageUserUseCase;
import com.daniel.mychickenbreastshop.user.application.port.in.UserSearchUseCase;
import com.daniel.mychickenbreastshop.user.domain.enums.Role;
import com.daniel.mychickenbreastshop.user.model.dto.request.UserSearchDto;
import com.daniel.mychickenbreastshop.user.model.dto.response.ListResponseDto;
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

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserAdminApiController.class)
class UserAdminApiControllerTest extends ControllerTest {

    @MockBean
    private ManageUserUseCase manageUserService;

    @MockBean
    private UserSearchUseCase userSearchService;

    @BeforeEach
    void setUp(RestDocumentationContextProvider provider) {
        mockMvc =
                MockMvcBuilders.standaloneSetup(new UserAdminApiController(manageUserService, userSearchService))
                        .addFilters(new CharacterEncodingFilter(StandardCharsets.UTF_8.displayName(), true))
                        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                        .apply(documentationConfiguration(provider))
                        .alwaysDo(MockMvcResultHandlers.print())
                        .alwaysDo(restDocs)
                        .build();
    }

    @DisplayName("API 요청을 통해 회원 목록을 반환한다.")
    @Test
    void getAll() throws Exception {
        // given
        int page = 1;
        List<ListResponseDto> listResponseDtos = getListResponseDtos();
        Role role = Role.ROLE_USER;

        given(userSearchService.getAllUsers(any(Pageable.class), any(Role.class)))
                .willReturn(listResponseDtos);

        // when
        ResultActions resultActions =
                mockMvc.perform(get("/api/v2/users/{role}", role)
                        .param("page", String.valueOf(page)));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(createJson(listResponseDtos)))
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("role").description("회원 등급")
                        ),
                        requestParameters(
                                parameterWithName("page").optional().description("요청 페이지 번호")
                        ),
                        responseFields(
                                fieldWithPath("[].userId").type(JsonFieldType.NUMBER).description("회원 아이디"),
                                fieldWithPath("[].loginId").type(JsonFieldType.STRING).description("로그인 아이디"),
                                fieldWithPath("[].name").type(JsonFieldType.STRING).description("회원 이름"),
                                fieldWithPath("[].email").type(JsonFieldType.STRING).description("회원 이메일"),
                                fieldWithPath("[].address").type(JsonFieldType.STRING).description("주소"),
                                fieldWithPath("[].zipcode").type(JsonFieldType.STRING).description("우편번호"),
                                fieldWithPath("[].role").type(JsonFieldType.VARIES).description("회원 등급"),
                                fieldWithPath("[].createdAt").type(JsonFieldType.ARRAY).description("회원 가입 일시"),
                                fieldWithPath("[].updatedAt").type(JsonFieldType.ARRAY).description("마지막 정보 수정 일시"),
                                fieldWithPath("[].deletedAt").type(JsonFieldType.ARRAY).description("마지막 탈퇴 일시")
                        )
                ));
    }

    @DisplayName("API 요청을 통해 검색 조건에 맞는 회원 목록을 반환한다.")
    @Test
    void searchUsers() throws Exception {
        // given
        int page = 1;
        List<ListResponseDto> listResponseDtos = getListResponseDtos();
        Role role = Role.ROLE_USER;

        UserSearchDto searchDto = UserSearchDto.builder()
                .searchKey("name")
                .searchValue("name")
                .build();

        MultiValueMap<String, String> params = createParams(searchDto);

        given(userSearchService.searchUsers(any(Pageable.class), any(Role.class), any(UserSearchDto.class)))
                .willReturn(listResponseDtos);

        // when
        ResultActions resultActions =
                mockMvc.perform(get("/api/v2/users/details/{role}", role)
                        .params(params));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(createJson(listResponseDtos)))
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("role").description("회원 등급")
                        ),
                        requestParameters(
                                parameterWithName("page").optional().description("요청 페이지 번호"),
                                parameterWithName("searchKey").description("검색 조건"),
                                parameterWithName("searchValue").description("검색 내용")
                        ),
                        responseFields(
                                fieldWithPath("[].userId").type(JsonFieldType.NUMBER).description("회원 아이디"),
                                fieldWithPath("[].loginId").type(JsonFieldType.STRING).description("로그인 아이디"),
                                fieldWithPath("[].name").type(JsonFieldType.STRING).description("회원 이름"),
                                fieldWithPath("[].email").type(JsonFieldType.STRING).description("회원 이메일"),
                                fieldWithPath("[].address").type(JsonFieldType.STRING).description("주소"),
                                fieldWithPath("[].zipcode").type(JsonFieldType.STRING).description("우편번호"),
                                fieldWithPath("[].role").type(JsonFieldType.VARIES).description("회원 등급"),
                                fieldWithPath("[].createdAt").type(JsonFieldType.ARRAY).description("회원 가입 일시"),
                                fieldWithPath("[].updatedAt").type(JsonFieldType.ARRAY).description("마지막 정보 수정 일시"),
                                fieldWithPath("[].deletedAt").type(JsonFieldType.ARRAY).description("마지막 탈퇴 일시")
                        )
                ));
    }

    @WithAuthUser
    @DisplayName("API 요청을 통해 회원 삭제를 진행한다.")
    @Test
    void removeUser() throws Exception {
        // given
        long userId = 1;

        // when
        ResultActions resultActions =
                mockMvc.perform(delete("/api/v2/users/{userId}", userId));

        // then
        resultActions
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("userId").description("회원 아이디")
                        )
                ));
    }

    private List<ListResponseDto> getListResponseDtos() {
        List<ListResponseDto> listResponseDtos = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            ListResponseDto listResponseDto = ListResponseDto.builder()
                    .userId(i)
                    .loginId("loginId" + i)
                    .name("name" + i)
                    .email("email" + i + "@naver.com")
                    .address("address" + i)
                    .zipcode("1232" + i)
                    .role(Role.ROLE_USER)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .deletedAt(LocalDateTime.now().plusDays(100))
                    .build();

            listResponseDtos.add(listResponseDto);
        }
        return listResponseDtos;
    }
}