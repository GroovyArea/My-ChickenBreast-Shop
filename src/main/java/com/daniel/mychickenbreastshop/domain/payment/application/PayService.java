package com.daniel.mychickenbreastshop.domain.payment.application;

import com.daniel.mychickenbreastshop.domain.payment.application.payment.PayApplicantFactory;
import com.daniel.mychickenbreastshop.domain.payment.application.payment.PaymentRequest;
import com.daniel.mychickenbreastshop.domain.payment.domain.order.OrderRepository;
import com.daniel.mychickenbreastshop.domain.payment.domain.order.OrderedProductRepository;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.CardRepository;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.PayRepository;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.request.ItemPayRequestDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.request.PayCancelRequestDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.response.kakaopay.ApiPayInfoDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.model.PaymentApi;
import com.daniel.mychickenbreastshop.domain.product.domain.item.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PayService {

    private final PayApplicantFactory payApplicantFactory;
    private final ProductRepository productRepository;
    private final PayRepository payRepository;
    private final OrderRepository orderRepository;
    private final CardRepository cardRepository;
    private final OrderedProductRepository orderedProductRepository;

    @Transactional
    public String getSingleItemPayURL(ItemPayRequestDto itemPayRequestDto, String requestUrl, String loginId, PaymentApi paymentApi) {
        PaymentRequest applicant = getPaymentRequest(paymentApi);

        /**
         * DB 저장 로직
         */
        return applicant.payItem(itemPayRequestDto, requestUrl, loginId).getNextRedirectPcUrl();
    }

    @Transactional
    public String getCartItemsPayURL(String cookieValue, String requestURL, String loginId, PaymentApi paymentApi) {
        PaymentRequest applicant = getPaymentRequest(paymentApi);
        /**
         *  DB 저장 로직
         */
        return applicant.payCart(cookieValue, requestURL, loginId).getNextRedirectPcUrl();
    }

    @Transactional // 보류 결제 조회 API 데이터를 필터 없이 그대로 준다는 것이 애매하다.
    public ApiPayInfoDto getApiPaymentDetail(String franchiseeId, String payId, PaymentApi paymentApi) {
        PaymentRequest applicant = getPaymentRequest(paymentApi);
        return null;
    }

    @Transactional
    public void approvePayment(String payToken, PaymentApi paymentApi, String loginId) {
        PaymentRequest applicant = getPaymentRequest(paymentApi);
        applicant.completePayment(payToken, loginId);
    }

    @Transactional
    public void cancelPayment(PayCancelRequestDto payCancelRequestDto, PaymentApi paymentApi) {
        PaymentRequest applicant = getPaymentRequest(paymentApi);
        applicant.cancelPayment(payCancelRequestDto);
    }

    private PaymentRequest getPaymentRequest(PaymentApi paymentApi) {
        return payApplicantFactory.findStrategy(paymentApi);
    }
}
