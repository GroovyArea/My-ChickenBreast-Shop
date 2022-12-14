package com.daniel.mychickenbreastshop.order.adaptor.in.web.rest;

import com.daniel.mychickenbreastshop.WithAuthUser;
import com.daniel.mychickenbreastshop.global.util.JsonUtil;
import com.daniel.mychickenbreastshop.order.application.port.in.ManageOrderUseCase;
import com.daniel.mychickenbreastshop.order.model.dto.request.OrderRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(OrderApiController.class)
class OrderApiControllerTest {

    @MockBean
    private ManageOrderUseCase manageOrderService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentationContextProvider) {
        mockMvc =
                MockMvcBuilders.standaloneSetup(new OrderApiController(manageOrderService))
                        .addFilters(new CharacterEncodingFilter("UTF-8", true))
                        .apply(documentationConfiguration(restDocumentationContextProvider))
                        .build();
    }

    @DisplayName("API ????????? ?????? ?????? ??? ????????? ??????.")
    @WithAuthUser
    @Test
    void createOrder() throws Exception {
        // given
        List<OrderRequestDto> orderRequestDtos = getOrderRequestDtos();
        Long orderId = 1L;

        given(manageOrderService.makeOrderReady(any(), anyLong()))
                .willReturn(orderId);

        // when & then
        mockMvc.perform(post("/api/v1/orders")
                        .content(parseObject(orderRequestDtos))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(orderId)))
                .andDo(print());
    }

    private String parseObject(Object object) {
        return JsonUtil.objectToString(object);
    }

    private List<OrderRequestDto> getOrderRequestDtos() {
        List<OrderRequestDto> orderRequestDtos = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            OrderRequestDto orderRequestDto = OrderRequestDto.builder()
                    .itemNumber((long) i)
                    .itemName("item_name" + i)
                    .itemImageUrl("item_image_download_url_" + i)
                    .quantity(100 * i)
                    .totalAmount(100000 * i)
                    .build();

            orderRequestDtos.add(orderRequestDto);
        }

        return orderRequestDtos;
    }
}