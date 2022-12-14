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

    @DisplayName("API ????????? ?????? ?????? ????????? ????????????.")
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
                                parameterWithName("role").description("?????? ??????")
                        ),
                        requestParameters(
                                parameterWithName("page").optional().description("?????? ????????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("[].userId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                fieldWithPath("[].loginId").type(JsonFieldType.STRING).description("????????? ?????????"),
                                fieldWithPath("[].name").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("[].email").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("[].address").type(JsonFieldType.STRING).description("??????"),
                                fieldWithPath("[].zipcode").type(JsonFieldType.STRING).description("????????????"),
                                fieldWithPath("[].role").type(JsonFieldType.VARIES).description("?????? ??????"),
                                fieldWithPath("[].createdAt").type(JsonFieldType.ARRAY).description("?????? ?????? ??????"),
                                fieldWithPath("[].updatedAt").type(JsonFieldType.ARRAY).description("????????? ?????? ?????? ??????"),
                                fieldWithPath("[].deletedAt").type(JsonFieldType.ARRAY).description("????????? ?????? ??????")
                        )
                ));
    }

    @DisplayName("API ????????? ?????? ?????? ????????? ?????? ?????? ????????? ????????????.")
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
                                parameterWithName("role").description("?????? ??????")
                        ),
                        requestParameters(
                                parameterWithName("page").optional().description("?????? ????????? ??????"),
                                parameterWithName("searchKey").description("?????? ??????"),
                                parameterWithName("searchValue").description("?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("[].userId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                fieldWithPath("[].loginId").type(JsonFieldType.STRING).description("????????? ?????????"),
                                fieldWithPath("[].name").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("[].email").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("[].address").type(JsonFieldType.STRING).description("??????"),
                                fieldWithPath("[].zipcode").type(JsonFieldType.STRING).description("????????????"),
                                fieldWithPath("[].role").type(JsonFieldType.VARIES).description("?????? ??????"),
                                fieldWithPath("[].createdAt").type(JsonFieldType.ARRAY).description("?????? ?????? ??????"),
                                fieldWithPath("[].updatedAt").type(JsonFieldType.ARRAY).description("????????? ?????? ?????? ??????"),
                                fieldWithPath("[].deletedAt").type(JsonFieldType.ARRAY).description("????????? ?????? ??????")
                        )
                ));
    }

    @WithAuthUser
    @DisplayName("API ????????? ?????? ?????? ????????? ????????????.")
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
                                parameterWithName("userId").description("?????? ?????????")
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