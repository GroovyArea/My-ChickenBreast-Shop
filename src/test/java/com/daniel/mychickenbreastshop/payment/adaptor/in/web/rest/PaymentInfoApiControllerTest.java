package com.daniel.mychickenbreastshop.payment.adaptor.in.web.rest;

import com.daniel.mychickenbreastshop.global.util.JsonUtil;
import com.daniel.mychickenbreastshop.payment.application.port.in.GetPaymentInfoUseCase;
import com.daniel.mychickenbreastshop.payment.domain.enums.PayStatus;
import com.daniel.mychickenbreastshop.payment.domain.enums.PaymentType;
import com.daniel.mychickenbreastshop.payment.model.dto.response.PaymentInfoResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;

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
@WebMvcTest(PaymentInfoApiController.class)
class PaymentInfoApiControllerTest {

    @MockBean
    private GetPaymentInfoUseCase paymentInfoUseCase;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentationContextProvider) {
        mockMvc =
                MockMvcBuilders.standaloneSetup(new PaymentInfoApiController(paymentInfoUseCase))
                        .addFilters(new CharacterEncodingFilter("UTF-8", true))
                        .apply(documentationConfiguration(restDocumentationContextProvider))
                        .alwaysDo(document("{method-name}", preprocessRequest(prettyPrint())))
                        .build();
    }

    @DisplayName("API 요청을 통해 결제 건에 대한 데이터를 반환한다.")
    @Test
    void getPaymentInfo() throws Exception {
        // given
        long paymentId = 1;
        PaymentInfoResponseDto paymentInfoResponseDto = getPaymentInfoResponseDto();

        given(paymentInfoUseCase.getPaymentDetail(paymentId))
                .willReturn(paymentInfoResponseDto);

        // when & then
        mockMvc.perform(get("/api/v1/payments/{paymentId}",paymentId))
                .andExpect(status().isOk())
                .andExpect(content().string(parseObject(paymentInfoResponseDto)))
                .andDo(print());
    }

    private String parseObject(Object object) {
        return JsonUtil.objectToString(object);
    }

    private PaymentInfoResponseDto getPaymentInfoResponseDto() {
        return PaymentInfoResponseDto.builder()
                .paymentId(1)
                .totalPrice(100000)
                .paymentType(PaymentType.CARD)
                .payStatus(PayStatus.COMPLETED)
                .cardId(1)
                .cardBin("card_bin_no")
                .cardType("cart_type")
                .installMonth(String.valueOf(LocalDateTime.now()))
                .interestFreeInstall("interest_free_install")
                .build();
    }
}