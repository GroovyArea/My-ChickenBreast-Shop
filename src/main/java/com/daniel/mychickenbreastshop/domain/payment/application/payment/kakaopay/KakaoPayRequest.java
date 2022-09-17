package com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay;

import com.daniel.mychickenbreastshop.domain.payment.application.payment.PaymentRequest;
import com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.webclient.KakaoPayClient;
import com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.webclient.config.KakaoPayClientProperty;
import com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.webclient.model.KakaoPayRequest.PayApproveRequest;
import com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.webclient.model.KakaoPayRequest.PayReadyRequest;
import com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.webclient.model.KakaoPayResponse.PayApproveResponse;
import com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.webclient.model.KakaoPayResponse.PayCancelResponse;
import com.daniel.mychickenbreastshop.domain.payment.application.payment.kakaopay.webclient.model.KakaoPayResponse.PayReadyResponse;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.request.ItemPayRequestDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.request.PayCancelRequestDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.model.PaymentApi;
import com.daniel.mychickenbreastshop.domain.payment.extract.CartDisassembler;
import com.daniel.mychickenbreastshop.domain.payment.extract.model.CartItem;
import com.daniel.mychickenbreastshop.domain.payment.extract.model.CartValue;
import com.daniel.mychickenbreastshop.global.store.RedisStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

import static com.daniel.mychickenbreastshop.domain.payment.domain.pay.model.PaymentApi.KAKAO;

/**
 * 카카오 페이 API 요청 객체
 */
@Component
@RequiredArgsConstructor
public class KakaoPayRequest implements PaymentRequest {

    private final KakaoPayClient kakaoPayClient;
    private final KakaoPayClientProperty kakaoPayClientProperty;
    private final CartDisassembler cartDisassembler;
    private final RedisStore redisStore;

    @Override
    public PaymentApi getPaymentApiName() {
        return KAKAO;
    }

    @Override // 일단 보류.
    public <T> T getOrderInfo(String franchiseeId, String payId, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> T payItem(ItemPayRequestDto itemPayRequestDto, String requestUrl, String loginId, Class<T> clazz) {
        PayReadyRequest request = createItemPayRequest(itemPayRequestDto, requestUrl, loginId);
        return kakaoPayClient.ready(kakaoPayClientProperty.getUri().getReady(), request, clazz);
    }

    @Override
    public <T> T payCart(String cookieValue, String requestUrl, String loginId, Class<T> clazz) {
        CartValue cartValue = getCartValue(cookieValue);
        PayReadyRequest request = createCartPayRequest(cartValue, requestUrl, loginId);
        return kakaoPayClient.ready(kakaoPayClientProperty.getUri().getReady(), request, clazz);
    }

    private void savePayableData(PayReadyResponse response, PayReadyRequest request, String loginId) {
        String payableParams = String.join(",", Stream.of(response.getTid(), request.getPartnerOrderId(), String.valueOf(request.getTotalAmount())).toArray(String[]::new));
        redisStore.setData(loginId, payableParams);
    }

    @Override
    public PayApproveResponse completePayment(String payToken, String loginId) {
        String[] savedParams = redisStore.getData(loginId).split(",");
        PayApproveRequest request = createPayApproveRequest(payToken, savedParams, loginId);
        return kakaoPayClient.approve(kakaoPayClientProperty.getUri().getApprove(), request);
    }

    @Override
    public PayCancelResponse cancelPayment(PayCancelRequestDto payCancelRequestDto) {

    }

    private PayReadyRequest createItemPayRequest(ItemPayRequestDto dto, String requestUrl, String loginId) {
        String orderId = loginId + " / " + dto.getItemName();

        return PayReadyRequest.builder()
                .partnerOrderId(orderId)
                .partnerUserId(loginId)
                .itemName(dto.getItemName())
                .quantity(dto.getQuantity())
                .totalAmount(dto.getTotalAmount())
                .taxFreeAmount(kakaoPayClientProperty.getParameter().getTaxFree())
                .cid(kakaoPayClientProperty.getParameter().getCid())
                .approvedUrl(requestUrl + kakaoPayClientProperty.getApi().getApproval())
                .cancelUrl(requestUrl + kakaoPayClientProperty.getApi().getCancel())
                .failUrl(requestUrl + kakaoPayClientProperty.getApi().getFail())
                .build();
    }

    private PayReadyRequest createCartPayRequest(CartValue cartValue, String requestUrl, String loginId) {
        String itemName = cartValue.getItemNames().get(0) + " 그 외 " + (cartValue.getItemNames().size() - 1) + "개";
        String orderId = loginId + " / " + itemName;
        String itemCode = String.join(", ", cartValue.getItemNumbers().stream().map(String::valueOf).toArray(String[]::new));

        return PayReadyRequest.builder()
                .partnerOrderId(orderId)
                .partnerUserId(loginId)
                .itemName(itemName)
                .itemCode(itemCode)
                .quantity(cartValue.getItemQuantities().size())
                .totalAmount((int) cartValue.getTotalPrice())
                .taxFreeAmount(kakaoPayClientProperty.getParameter().getTaxFree())
                .cid(kakaoPayClientProperty.getParameter().getCid())
                .approvedUrl(kakaoPayClientProperty.getApi().getApproval())
                .cancelUrl(kakaoPayClientProperty.getApi().getCancel())
                .failUrl(kakaoPayClientProperty.getApi().getFail())
                .build();
    }

    private PayApproveRequest createPayApproveRequest(String payToken, String[] params, String loginId) {
        String tid = params[0];
        String orderId = params[1];
        Integer totalPrice = Integer.valueOf(params[2]);

        return PayApproveRequest.builder()
                .cid(kakaoPayClientProperty.getParameter().getCid())
                .tid(tid)
                .partnerOrderId(orderId)
                .partnerUserId(loginId)
                .pgToken(payToken)
                .totalAmount(totalPrice)
                .build();
    }

    private CartValue getCartValue(String cookieValue) {
        return CartValue.builder()
                .itemNumbers(cartDisassembler.getItemNumbers(cookieValue, Long.class, CartItem.class, CartItem::getItemNo))
                .itemNames(cartDisassembler.getItemNames(cookieValue, Long.class, CartItem.class, CartItem::getItemName))
                .itemQuantities(cartDisassembler.getItemQuantities(cookieValue, Long.class, CartItem.class, CartItem::getItemQuantity))
                .totalPrice(cartDisassembler.getTotalPrice(cookieValue, Long.class, CartItem.class, CartItem::getTotalPrice))
                .build();
    }

}
