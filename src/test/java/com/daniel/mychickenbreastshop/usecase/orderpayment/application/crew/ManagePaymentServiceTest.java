package com.daniel.mychickenbreastshop.usecase.orderpayment.application.crew;

import com.daniel.mychickenbreastshop.payment.application.port.in.ManagePaymentUseCase;
import com.daniel.mychickenbreastshop.payment.application.service.gateway.kakaopay.webclient.model.KakaoPayResponse;
import com.daniel.mychickenbreastshop.payment.application.service.gateway.model.PaymentRequestResult;
import com.daniel.mychickenbreastshop.payment.application.service.gateway.model.PaymentResult;
import com.daniel.mychickenbreastshop.payment.application.service.gateway.model.enums.PaymentGateway;
import com.daniel.mychickenbreastshop.payment.application.service.strategy.PaymentStrategyFactory;
import com.daniel.mychickenbreastshop.payment.application.service.strategy.PaymentStrategyService;
import com.daniel.mychickenbreastshop.payment.model.dto.request.ItemPayRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ManagePaymentServiceTest {

    @Mock
    private PaymentStrategyFactory paymentStrategyFactory;

    @Mock
    private PaymentStrategyService<PaymentResult> paymentStrategyService;

    @InjectMocks
    private ManagePaymentUseCase paymentUseCase;

    private String requestUrl;
    private String loginId;
    private long orderId;
    private PaymentGateway paymentGateway;

    @BeforeEach
    void setUp() {
        loginId = "loginId";
        orderId = 1;
        requestUrl = "localhost:8080/";
        paymentGateway = PaymentGateway.KAKAO;
    }

    @DisplayName("외부 결제 API에 상품 목록 결제에 대한 응답 결과 중 Redirect URL 일치 여부를 판단한다.")
    @Test
    void getSingleItemPayResultUrl() {
        // given
        List<ItemPayRequestDto> itemPayRequestDtos = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            ItemPayRequestDto itemPayRequestDto = ItemPayRequestDto.builder()
                    .itemNumber(1L)
                    .quantity(100)
                    .itemName("itemName1")
                    .totalAmount(1000)
                    .build();

            itemPayRequestDtos.add(itemPayRequestDto);
        }

        String redirectUrl = requestUrl + "approve";

        PaymentResult payReadyResponse = KakaoPayResponse.PayReadyResponse.builder()
                .nextRedirectPcUrl(redirectUrl).build();

        // when
        when(paymentStrategyFactory.findStrategy(any(PaymentGateway.class)))
                .thenReturn(paymentStrategyService);
        when(paymentStrategyService.payItems(itemPayRequestDtos, requestUrl, loginId, orderId))
                .thenReturn(payReadyResponse);

        PaymentRequestResult paymentReady = (PaymentRequestResult) paymentUseCase
                .createPaymentReady(itemPayRequestDtos, paymentGateway, requestUrl, loginId, orderId);

        assertThat(paymentReady.getRedirectUrl()).isEqualTo(redirectUrl);
    }

}