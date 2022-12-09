package com.daniel.mychickenbreastshop.usecase.orderpayment.application.crew;

import com.daniel.mychickenbreastshop.payment.application.port.in.ManagePaymentUseCase;
import com.daniel.mychickenbreastshop.payment.application.service.gateway.kakaopay.webclient.model.KakaoPayResponse;
import com.daniel.mychickenbreastshop.payment.application.service.gateway.model.PaymentResult;
import com.daniel.mychickenbreastshop.payment.application.service.strategy.PaymentStrategyFactory;
import com.daniel.mychickenbreastshop.payment.application.service.strategy.PaymentStrategyService;
import com.daniel.mychickenbreastshop.payment.model.dto.request.ItemPayRequestDto;
import com.daniel.mychickenbreastshop.payment.model.enums.PaymentGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ManagePaymentUseServiceTest {

    @Mock
    private PaymentStrategyFactory paymentStrategyFactory;

    @Mock
    private PaymentStrategyService<PaymentResult> paymentStrategyService;


    @InjectMocks
    private ManagePaymentUseCase paymentUseCase;

    private String requestUrl;
    private String loginId;
    private PaymentGateway paymentGateway;

    @BeforeEach
    void setUp() {
        loginId = "loginId";
        requestUrl = "localhost:8080/";
        paymentGateway = PaymentGateway.KAKAO;
    }

    @DisplayName("외부 결제 API에 단건 결제의 요청 URL을 반환한다.")
    @Test
    void getSingleItemPayResultUrl() {
        // given
        ItemPayRequestDto itemPayRequestDto = ItemPayRequestDto.builder()
                .itemNumber(1L)
                .quantity(100)
                .itemName("itemName1")
                .totalAmount(1000)
                .build();

        String redirectUrl = requestUrl + "approve";

        KakaoPayResponse.PayReadyResponse payReadyResponse = KakaoPayResponse.PayReadyResponse.builder()
                .nextRedirectPcUrl(redirectUrl).build();

        // when
        when(paymentStrategyFactory.findStrategy(any(PaymentGateway.class))).thenReturn(paymentStrategyService);
        when(paymentStrategyService.payItems(itemPayRequestDto, requestUrl, loginId)).thenReturn(payReadyResponse);

        String singleItemPayResultUrl = paymentApplicationCrew.getSingleItemPayUrl(itemPayRequestDto, requestUrl, loginId, paymentGateway);

        assertThat(singleItemPayResultUrl).isEqualTo(redirectUrl);
    }

    @DisplayName("외부 결제 API에 장바구니 결제 요청 URL을 반환한다.")
    @Test
    void getCartItemsPayUrl() {
        // given
        String cookieValue = "cookieValue";

        String redirectUrl = requestUrl + "approve";

        PayReadyResponse payReadyResponse = PayReadyResponse.builder()
                .nextRedirectPcUrl(redirectUrl).build();

        // when
        when(paymentStrategyFactory.findStrategy(any(PaymentGateway.class))).thenReturn(paymentStrategyApplication);
        when(paymentStrategyApplication.payCart(cookieValue, requestUrl, loginId)).thenReturn(payReadyResponse);

        String cartItemsPayUrl = paymentApplicationCrew.getCartItemsPayUrl(cookieValue, requestUrl, loginId, paymentGateway);

        assertThat(cartItemsPayUrl).isEqualTo(redirectUrl);
    }

}