package com.daniel.mychickenbreastshop.domain.user.api;

import com.daniel.mychickenbreastshop.auth.security.model.PrincipalDetails;
import com.daniel.mychickenbreastshop.domain.user.application.UserService;
import com.daniel.mychickenbreastshop.domain.user.model.dto.request.ModifyRequestDto;
import com.daniel.mychickenbreastshop.domain.user.model.dto.request.UserSearchDto;
import com.daniel.mychickenbreastshop.domain.user.model.dto.response.DetailResponseDto;
import com.daniel.mychickenbreastshop.domain.user.model.dto.response.ListResponseDto;
import com.daniel.mychickenbreastshop.domain.user.model.enums.Role;
import com.daniel.mychickenbreastshop.global.util.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
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
@WebMvcTest(UserApiController.class)
@AutoConfigureRestDocs
class UserApiControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentationContextProvider) {
        mockMvc =
                MockMvcBuilders.standaloneSetup(new UserApiController(userService))
                        .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                        .apply(documentationConfiguration(restDocumentationContextProvider))
                        .alwaysDo(document("{method-name}", preprocessRequest(prettyPrint())))
                        .build();
    }

    @DisplayName("API 요청을 통해 회원 정보를 반환한다.")
    @Test
    void getUserDetail() throws Exception {
        // given
        DetailResponseDto dto = DetailResponseDto.builder()
                .userId(1L)
                .loginId("loginId")
                .name("name")
                .email("0909@gmail.com")
                .address("서울시 서초구 서초동")
                .zipcode("123-123")
                .role(Role.ROLE_USER)
                .build();

        given(userService.getUser(anyLong())).willReturn(dto);

        mockMvc.perform(get("/api/v1/users/{userId}", dto.getUserId()))
                .andExpect(status().isOk())
                .andExpect(content().string(JsonUtil.objectToString(dto)))
                .andDo(print());

    }

    @DisplayName("API 요청을 통해 회원 정보를 수정한다.")
    @Test
    void modifyUser() throws Exception {
        // given
        ModifyRequestDto dto = ModifyRequestDto.builder()
                .name("name")
                .password("update password")
                .email("234@gmail.com")
                .address("서울시 서초구 서초동")
                .zipcode("123-123")
                .build();

        UserDetails userDetails = PrincipalDetails.builder()
                .id(1L)
                .loginId("loginId")
                .password("password")
                .role(Role.ROLE_USER.getRoleName())
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        mockMvc.perform(patch("/api/v1/users")
                        .content(JsonUtil.objectToString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("API 요청을 통해 회원을 탈퇴 처리한다.")
    @Test
    void removeUser() throws Exception {
        // given
        Long userId = 1L;

        mockMvc.perform(delete("/api/v2/users/{userId}", userId))
                .andExpect(status().isOk());
    }

    @DisplayName("API 요청을 통해 해당 페이지의 10개의 회원 목록을 조회한다.")
    @Test
    void getAll() throws Exception {
        // given
        List<ListResponseDto> pageOneUsers = getPageUsers();
        int page = 1;

        given(userService.getAllUsers(any(Pageable.class))).willReturn(pageOneUsers);

        mockMvc.perform(get("/api/v2/users")
                        .param("page", String.valueOf(page)))
                .andExpect(status().isOk())
                .andExpect(content().string(JsonUtil.objectToString(pageOneUsers)))
                .andDo(print());
    }

    @DisplayName("API 요청을 통해 검색 조건을 충족한 회원 목록을 조회한다.")
    @Test
    void searchUsers() throws Exception {
        // given
        List<ListResponseDto> pageOneSearchUsers = getPageUsers();
        Role role = Role.ROLE_USER;

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("searchKey", "name");
        params.add("searchValue", "name");

        given(userService.searchUsers(any(Pageable.class), any(Role.class), any(UserSearchDto.class))).willReturn(pageOneSearchUsers);

        mockMvc.perform(get("/api/v2/users/search/{role}", role)
                        .params(params))
                .andExpect(status().isOk())
                .andExpect(content().string(JsonUtil.objectToString(pageOneSearchUsers)))
                .andDo(print());
    }

    List<ListResponseDto> getPageUsers() {
        List<ListResponseDto> users = new ArrayList();

        for (int i = 0; i < 10; i++) {
            ListResponseDto dto = ListResponseDto.builder()
                    .userId(i)
                    .loginId("loginId" + i)
                    .name("name" + i)
                    .email("email" + i + "@gmail.com")
                    .address("서울시 서초구 서초 " + i + "동")
                    .zipcode("123-12" + i)
                    .role(Role.ROLE_USER)
                    .updatedAt(LocalDateTime.now())
                    .createdAt(LocalDateTime.now())
                    .build();

            users.add(dto);
        }

        return users;
    }
}