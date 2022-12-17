package com.daniel.mychickenbreastshop.payment.adaptor.in.web.rest;

import com.daniel.mychickenbreastshop.payment.application.port.in.GetPaymentInfoUseCase;
import com.daniel.mychickenbreastshop.payment.model.dto.response.PaymentInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 결제 API
 * 외부 결제 API 사용
 * <pre>
 *     <b>history</b>
 *     1.0 2022.09.13 최초작성
 *     1.1 2022.12.07 패키지 구조 변경
 * </pre>
 *
 * @author Daniel Kim
 * @version 1.1
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentInfoApiController {

    private final GetPaymentInfoUseCase paymentInfoUseCase;

    /**
     * 결제 정보(+ 카드 정보) 조회
     */
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentInfoResponseDto> getPaymentInfo(@PathVariable Long paymentId) {
        PaymentInfoResponseDto paymentInfoResponseDto = paymentInfoUseCase.getPaymentDetail(paymentId);
        return ResponseEntity.ok(paymentInfoResponseDto);
    }
}
