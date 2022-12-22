package com.daniel.mychickenbreastshop.user.adaptor.in.web.rest;

import com.daniel.mychickenbreastshop.global.util.JsonUtil;
import com.daniel.mychickenbreastshop.user.application.port.in.ManageUserUseCase;
import com.daniel.mychickenbreastshop.user.application.port.in.UserSearchUseCase;
import com.daniel.mychickenbreastshop.user.domain.enums.Role;
import com.daniel.mychickenbreastshop.user.model.dto.request.UserSearchDto;
import com.daniel.mychickenbreastshop.user.model.dto.response.ListResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserAdminApiController.class)
@ExtendWith(RestDocumentationExtension.class)
class UserAdminApiControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private ManageUserUseCase manageUserService;

    @MockBean
    private UserSearchUseCase userSearchService;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentationContextProvider) {
        mockMvc =
                MockMvcBuilders.standaloneSetup(new UserAdminApiController(manageUserService, userSearchService))
                        .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                        .apply(documentationConfiguration(restDocumentationContextProvider))
                        .build();
    }


    @DisplayName("API 요청을 통해 회원 목록을 반환한다.")
    @Test
    void getAll() throws Exception {
        // given
        List<ListResponseDto> listResponseDtos = getListResponseDtos();
        int page = 1;
        Role role = Role.ROLE_USER;

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", String.valueOf(page));

        given(userSearchService.getAllUsers(any(Pageable.class), any(Role.class)))
                .willReturn(listResponseDtos);

        mockMvc.perform(get("/api/v2/users/{role}", role)
                        .params(params))
                .andExpect(status().isOk())
                .andExpect(content().string(parseObject(listResponseDtos)))
                .andDo(print());
    }

    @DisplayName("API 요청을 통해 검색 조건에 맞는 회원 목록을 반환한다.")
    @Test
    void searchUsers() throws Exception {
        // given
        List<ListResponseDto> listResponseDtos = getListResponseDtos();
        int page = 1;
        Role role = Role.ROLE_USER;

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", String.valueOf(page));
        params.add("searchKey", "name");
        params.add("searchValue", "name");

        given(userSearchService.searchUsers(any(Pageable.class), any(Role.class), any(UserSearchDto.class)))
                .willReturn(listResponseDtos);

        // when & then
        mockMvc.perform(get("/api/v2/users/details/{role}", role)
                        .params(params))
                .andExpect(status().isOk())
                .andExpect(content().string(parseObject(listResponseDtos)))
                .andDo(print());
    }

    @DisplayName("API 요청을 통해 회원 삭제를 진행한다.")
    @Test
    void removeUser() throws Exception {
        // given
        long userId = 1;

        mockMvc.perform(delete("/api/v2/users/{userId}", userId))
                .andExpect(status().isOk());
    }

    private String parseObject(Object object) {
        return JsonUtil.objectToString(object);
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
                    .build();

            listResponseDtos.add(listResponseDto);
        }
        return listResponseDtos;
    }
}