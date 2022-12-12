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
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
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
@WebMvcTest(OrderInfoApiController.class)
@AutoConfigureRestDocs
class OrderInfoApiControllerTest {

    @MockBean
    private GetOrderInfoUseCase orderInfoUseCase;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentationContextProvider) {
        mockMvc =
                MockMvcBuilders.standaloneSetup(new OrderInfoApiController(orderInfoUseCase))
                        .addFilters(new CharacterEncodingFilter("UTF-8", true))
                        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                        .apply(documentationConfiguration(restDocumentationContextProvider))
                        .alwaysDo(document("{method-name}", preprocessRequest(prettyPrint())))
                        .build();
    }

    @DisplayName("API 호출을 통해 회원 개인의 주문 내역 목록 10개를 조회한다.")
    @Test
    void getAllOrders() throws Exception {
        // given
        long userId = 1;
        OrderStatus orderStatus = OrderStatus.ORDER_COMPLETE;
        List<OrderInfoListResponseDto> listResponseDtos = getListResponseDtos();
        int page = 1;

        given(orderInfoUseCase.getAllOrders(userId, orderStatus, any(Pageable.class)))
                .willReturn(listResponseDtos);

        // when & then
        mockMvc.perform(get("/api/v1/orders/user/{userId}", userId)
                        .param("page", String.valueOf(page))
                        .param("orderStatus", orderStatus.name()))
                .andExpect(status().isOk())
                .andExpect(content().string(parseObject(listResponseDtos)))
                .andDo(print());
    }

    @DisplayName("API 요청을 통해 주문 건에 대한 상세 조회 데이터를 반환한다.")
    @Test
    void getOrderInfo() throws Exception {
        // given
        OrderItemsInfoResponseDto orderItemsInfoResponseDto = getOrderItemsInfoResponseDto();
        long orderId = 1;

        given(orderInfoUseCase.getOrderDetail(orderId))
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

        for (int i = 1; i <= 30; i++) {
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