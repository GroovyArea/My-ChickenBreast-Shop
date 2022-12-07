package com.daniel.ddd.payment.application.service.strategy.kakaopay;

import com.daniel.ddd.global.aspect.annotation.RedisLocked;
import com.daniel.ddd.global.error.exception.BadRequestException;
import com.daniel.ddd.payment.adaptor.out.persistence.PaymentRepository;
import com.daniel.ddd.payment.application.event.builder.PaymentProcessEventBuilder;
import com.daniel.ddd.payment.application.event.model.ItemsVariation;
import com.daniel.ddd.payment.application.event.model.OrderVariation;
import com.daniel.ddd.payment.application.event.model.PaymentEvent;
import com.daniel.ddd.payment.application.service.gateway.kakaopay.application.KakaoPaymentApplicationService;
import com.daniel.ddd.payment.application.service.gateway.kakaopay.webclient.model.KakaoPayResponse;
import com.daniel.ddd.payment.application.service.gateway.model.PaymentResult;
import com.daniel.ddd.payment.application.service.strategy.PaymentStrategyService;
import com.daniel.ddd.payment.domain.Card;
import com.daniel.ddd.payment.domain.Payment;
import com.daniel.ddd.payment.domain.enums.ErrorMessages;
import com.daniel.ddd.payment.domain.enums.PayStatus;
import com.daniel.ddd.payment.domain.enums.PaymentType;
import com.daniel.ddd.payment.model.dto.request.ItemPayRequestDto;
import com.daniel.ddd.payment.model.dto.request.PayCancelRequestDto;
import com.daniel.ddd.payment.model.enums.PaymentGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static com.daniel.ddd.payment.model.enums.PaymentGateway.KAKAO;

/**
 * 전략 결제 API 서비스 구현체 (카카오페이)
 */
@Service
@RequiredArgsConstructor
public class KakaopayStrategyService implements PaymentStrategyService<PaymentResult> {

    private final KakaoPaymentApplicationService kakaoPaymentService;
    private final ApplicationEventPublisher eventPublisher;
    private final PaymentProcessEventBuilder<PaymentEvent> eventBuilder;

    private final PaymentRepository paymentRepository;

    @Override
    public PaymentGateway getPaymentGatewayName() {
        return KAKAO;
    }

    @Override
    public PaymentResult getOrderInfo(String franchiseeId, String payId, String requestUrl) {
        return kakaoPaymentService.getOrderInfo(franchiseeId, payId, requestUrl);
    }

    @Override
    @RedisLocked(lockKey = "pay_items")
    @Transactional
    public PaymentResult payItems(List<ItemPayRequestDto> itemPayRequestDtos, String requestUrl,
                                  String loginId, Long orderId) {
        KakaoPayResponse.PayReadyResponse response = kakaoPaymentService
                .payItems(itemPayRequestDtos, requestUrl, loginId);

        long totalAmount = itemPayRequestDtos.stream()
                .mapToLong(ItemPayRequestDto::getTotalAmount)
                .sum();
        Payment payment = Payment.createPayment(orderId, totalAmount);
        Long paymentId = paymentRepository.save(payment).getId();

        response.updatePaymentId(paymentId);
        return response;
    }

    @Override
    @RedisLocked(lockKey = "completePayment")
    @Transactional
    public PaymentResult completePayment(String payToken, Long paymentId, String loginId) {
        KakaoPayResponse.PayApproveResponse response = kakaoPaymentService.completePayment(payToken, loginId);

        Payment savedPayment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new BadRequestException((ErrorMessages.PAYMENT_NOT_EXISTS.getMessage())));

        if (response.getCardInfo() != null) {
            Card card = Card.createCard(
                    response.getCardInfo().getBin(),
                    response.getCardInfo().getCardType(),
                    response.getCardInfo().getInstallMonth(),
                    response.getCardInfo().getInterestFreeInstall());

            savedPayment.updatePaymentTypeInfo(PaymentType.CARD);
            savedPayment.updateCardInfo(card);
        } else {
            savedPayment.updatePaymentTypeInfo(PaymentType.CASH);
        }

        savedPayment.updatePaymentStatus(PayStatus.COMPLETED);

        eventPublisher.publishEvent(
                eventBuilder.createEvent(new OrderVariation(paymentId, true))
        );

        String[] itemCodes = response.getItemCode().split("/");
        List<String> itemNumbers = getItemNumbers(itemCodes);
        List<String> itemQuantities = getItemQuantities(itemCodes);

        eventPublisher.publishEvent(
                eventBuilder.createEvent(ItemsVariation.builder()
                        .numbers(itemNumbers.stream().map(Long::valueOf).toList())
                        .quantities(itemQuantities.stream().map(Integer::valueOf).toList())
                        .totalAmount(response.getAmount().getTotal())
                        .status(false)
                        .build())
        );

        return response;
    }

    @Override
    @RedisLocked(lockKey = "cancelPayment")
    @Transactional
    public PaymentResult cancelPayment(PayCancelRequestDto payCancelRequestDto, Long paymentId, String loginId) {
        KakaoPayResponse.PayCancelResponse response = kakaoPaymentService.cancelPayment(payCancelRequestDto);

        Payment savedPayment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new BadRequestException((ErrorMessages.PAYMENT_NOT_EXISTS.getMessage())));

        savedPayment.cancelPayment();

        String[] itemCodes = response.getItemCode().split("/");
        List<String> itemNumbers = getItemNumbers(itemCodes);
        List<String> itemQuantities = getItemQuantities(itemCodes);

        eventPublisher.publishEvent(
                eventBuilder.createEvent(new OrderVariation(paymentId, false))
        );

        eventPublisher.publishEvent(
                eventBuilder.createEvent(ItemsVariation.builder()
                        .numbers(itemNumbers.stream().map(Long::valueOf).toList())
                        .quantities(itemQuantities.stream().map(Integer::valueOf).toList())
                        .totalAmount(response.getAmount().getTotal())
                        .status(true)
                        .build())
        );

        return response;
    }

    private List<String> getItemQuantities(String[] itemCodes) {
        return Arrays.stream(itemCodes[1].split(",")).toList();
    }

    private List<String> getItemNumbers(String[] itemCodes) {
        return Arrays.stream(itemCodes[0].split(",")).toList();
    }

}
