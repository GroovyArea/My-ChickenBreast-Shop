package com.daniel.mychickenbreastshop.order.adaptor.in.web.rest;

import com.daniel.mychickenbreastshop.global.util.JsonUtil;
import com.daniel.mychickenbreastshop.order.application.port.in.GetOrderInfoUseCase;
import com.daniel.mychickenbreastshop.order.domain.enums.OrderStatus;
import com.daniel.mychickenbreastshop.order.model.dto.response.OrderInfoListResponseDto;
import com.daniel.mychickenbreastshop.order.model.dto.response.OrderItemsInfoResponseDto;
import com.daniel.mychickenbreastshop.order.model.dto.response.OrderProductResponseDto;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(OrderInfoApiController.class)
class OrderInfoApiControllerTest {

    @MockBean
    private GetOrderInfoUseCase getOrderInfoService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentationContextProvider) {
        mockMvc =
                MockMvcBuilders.standaloneSetup(new OrderInfoApiController(getOrderInfoService))
                        .addFilters(new CharacterEncodingFilter("UTF-8", true))
                        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                        .apply(documentationConfiguration(restDocumentationContextProvider))
                        .build();
    }

    @DisplayName("API 호출을 통해 회원 개인의 주문 내역 목록 10개를 조회한다.")
    @Test
    void getAllOrders() throws Exception {
        // given
        long userId = 1;
        OrderStatus orderStatus = OrderStatus.ORDER_COMPLETE;
        List<OrderInfoListResponseDto> orderInfoListResponseDtos = getListResponseDtos();
        int page = 1;

        given(getOrderInfoService.getAllOrders(anyLong(),
                any(OrderStatus.ORDER_COMPLETE.getClass()), any(Pageable.class)))
                .willReturn(orderInfoListResponseDtos);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", String.valueOf(page));
        params.add("orderStatus", orderStatus.name());

        // when & then
        mockMvc.perform(get("/api/v1/orders/user/{userId}", userId)
                        .params(params))
                .andExpect(status().isOk())
                .andExpect(content().string(parseObject(orderInfoListResponseDtos)))
                .andDo(print());
    }

    @DisplayName("API 요청을 통해 주문 건에 대한 상세 조회 데이터를 반환한다.")
    @Test
    void getOrderInfo() throws Exception {
        // given
        OrderItemsInfoResponseDto orderItemsInfoResponseDto = getOrderItemsInfoResponseDto();
        long orderId = 1;

        given(getOrderInfoService.getOrderDetail(orderId))
                .willReturn(orderItemsInfoResponseDto);

        mockMvc.perform(get("/api/v1/orders/{orderId}", orderId))
                .andExpect(status().isOk())
                .andExpect(content().string(parseObject(orderItemsInfoResponseDto)))
                .andDo(print());
    }

    private String parseObject(Object object) {
        return JsonUtil.objectToString(object);
    }

    private List<OrderInfoListResponseDto> getListResponseDtos() {
        List<OrderInfoListResponseDto> orderInfoListResponseDtos = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            OrderInfoListResponseDto orderInfoListResponseDto = OrderInfoListResponseDto.builder()
                    .orderId(i)
                    .paymentId(i)
                    .totalCount(10 * i)
                    .orderPrice(1000 * i)
                    .orderStatus(OrderStatus.ORDER_COMPLETE)
                    .createAt(LocalDateTime.now())
                    .build();

            orderInfoListResponseDtos.add(orderInfoListResponseDto);
        }

        return orderInfoListResponseDtos;
    }

    private OrderItemsInfoResponseDto getOrderItemsInfoResponseDto() {
        List<OrderProductResponseDto> orderProductResponseDtos = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            OrderProductResponseDto build = OrderProductResponseDto.builder()
                    .orderProductId(i)
                    .count(i)
                    .name("name" + i)
                    .price(1000 * i)
                    .image("order_product_image_url_" + i)
                    .build();

            orderProductResponseDtos.add(build);
        }

        return OrderItemsInfoResponseDto.builder()
                .orderId(1)
                .orderInfoResponseDtos(orderProductResponseDtos)
                .totalCount(55)
                .orderStatus(OrderStatus.ORDER_COMPLETE)
                .build();
    }

}