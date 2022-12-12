package com.daniel.mychickenbreastshop.user.adaptor.in.web.rest;

import com.daniel.mychickenbreastshop.global.util.JsonUtil;
import com.daniel.mychickenbreastshop.user.application.port.in.ManageUserUseCase;
import com.daniel.mychickenbreastshop.user.application.port.in.UserSearchUseCase;
import com.daniel.mychickenbreastshop.user.auth.security.model.PrincipalDetails;
import com.daniel.mychickenbreastshop.user.domain.enums.Role;
import com.daniel.mychickenbreastshop.user.model.dto.request.ModifyRequestDto;
import com.daniel.mychickenbreastshop.user.model.dto.response.DetailResponseDto;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(UserApiController.class)
@AutoConfigureRestDocs
class UserApiControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private ManageUserUseCase userUseService;

    @MockBean
    private UserSearchUseCase searchUseService;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentationContextProvider) {
        mockMvc =
                MockMvcBuilders.standaloneSetup(new UserAdminApiController(userUseService, searchUseService))
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

        given(searchUseService.getUser(anyLong())).willReturn(dto);

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
}