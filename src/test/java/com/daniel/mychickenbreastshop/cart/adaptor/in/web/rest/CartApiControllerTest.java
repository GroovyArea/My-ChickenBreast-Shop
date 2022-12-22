package com.daniel.mychickenbreastshop.cart.adaptor.in.web.rest;

import com.daniel.mychickenbreastshop.cart.application.port.in.ManageCartUseCase;
import com.daniel.mychickenbreastshop.cart.model.dto.response.CartResponseDto;
import com.daniel.mychickenbreastshop.global.util.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(CartApiController.class)
class CartApiControllerTest {

    @MockBean
    private ManageCartUseCase cartUseCase;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentationContextProvider) {
        mockMvc =
                MockMvcBuilders.standaloneSetup(new CartApiController(cartUseCase))
                        .addFilters(new CharacterEncodingFilter("UTF-8", true))
                        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                        .apply(documentationConfiguration(restDocumentationContextProvider))
                        .alwaysDo(document("{method-name}", preprocessRequest(prettyPrint())))
                        .build();
    }

    @DisplayName("API 요청을 통해 장바구니에 담긴 상품 목록을 반환한다.")
    @Test
    void getCartList() throws Exception {
        // given
        Cookie cookie = new Cookie("chicken", "value");
        List<CartResponseDto> cartResponseDtos = getCartResponseDtos();

        given(cartUseCase.getCart(cookie.getValue()))
                .willReturn(cartResponseDtos);

        mockMvc.perform(get("/api/v1/carts")
                        .cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(content().string(parseObject(cartResponseDtos)))
                .andDo(print());
    }

    private String parseObject(Object object) {
        return JsonUtil.objectToString(object);
    }

    private List<CartResponseDto> getCartResponseDtos() {
        List<CartResponseDto> cartResponseDtos = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            CartResponseDto cartResponseDto = CartResponseDto.builder()
                    .itemNo((long) i)
                    .itemName("item_name" + i)
                    .itemQuantity(i)
                    .totalPrice((long) (10000 * i))
                    .build();

            cartResponseDtos.add(cartResponseDto);
        }

        return cartResponseDtos;
    }
}