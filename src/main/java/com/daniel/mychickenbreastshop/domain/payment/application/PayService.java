package com.daniel.mychickenbreastshop.domain.payment.application;

import com.daniel.mychickenbreastshop.domain.order.domain.dto.request.ItemOrderRequestDto;
import com.daniel.mychickenbreastshop.domain.payment.application.payment.PayApplicantFactory;
import com.daniel.mychickenbreastshop.domain.payment.application.payment.PaymentApplicant;
import com.daniel.mychickenbreastshop.domain.payment.domain.order.OrderRepository;
import com.daniel.mychickenbreastshop.domain.payment.domain.order.OrderedProductRepository;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.CardRepository;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.PayRepository;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.request.ItemPayRequestDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.request.PayCancelRequestDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.response.PayApprovalResponseDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.response.kakaopay.ApiPayInfoDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.model.PaymentApi;
import com.daniel.mychickenbreastshop.domain.product.domain.item.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String getSingleItemPayURL(ItemPayRequestDto itemPayRequestDto, String requestURL, Long userId, PaymentApi paymentApi) {
        PaymentApplicant applicant = getPaymentApplicant(paymentApi);

        /**
         * DB 저장 로직
         */
        return applicant.payItem(itemPayRequestDto, requestURL, userId);
    }

    @Transactional
    public String getCartItemsPayURL(String cookieValue, String requestURL, Long userId, PaymentApi paymentApi) {
        PaymentApplicant applicant = getPaymentApplicant(paymentApi);

        /**
         *  DB 저장 로직
         */
        return applicant.payCart(cookieValue, requestURL, userId);
    }

    public ApiPayInfoDto getApiPaymentDetail(String franchiseeId, String payId, PaymentApi paymentApi) {
        PaymentApplicant applicant = getPaymentApplicant(paymentApi);

    }

    @Transactional
    public void approvePayment(String payToken, PaymentApi paymentApi) {
        PaymentApplicant applicant = getPaymentApplicant(paymentApi);

    }

    @Transactional
    public void cancelPayment(PayCancelRequestDto payCancelRequestDto, PaymentApi paymentApi) {
        PaymentApplicant applicant = getPaymentApplicant(paymentApi);

    }

    private PaymentApplicant getPaymentApplicant(PaymentApi paymentApi) {
        return payApplicantFactory.findStrategy(paymentApi);
    }
}
