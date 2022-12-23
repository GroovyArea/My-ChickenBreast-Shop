package com.daniel.mychickenbreastshop.payment.adaptor.in.web.rest;

import com.daniel.mychickenbreastshop.payment.adaptor.in.web.rest.dto.PaymentRequestionDto;
import com.daniel.mychickenbreastshop.payment.application.port.in.ManagePaymentUseCase;
import com.daniel.mychickenbreastshop.payment.application.service.gateway.model.PaymentRequestResult;
import com.daniel.mychickenbreastshop.payment.application.service.gateway.model.enums.PaymentGateway;
import com.daniel.mychickenbreastshop.payment.model.dto.request.ItemPayRequestDto;
import com.daniel.mychickenbreastshop.payment.model.dto.request.PayCancelRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static com.daniel.mychickenbreastshop.payment.domain.enums.ErrorMessages.CANCELED_PAY;
import static com.daniel.mychickenbreastshop.payment.domain.enums.ErrorMessages.FAILED_PAY;

/**
 * 결제 api 컨트롤러
 * 외부 결제 api 연계
 * <p>
 * 결제 요청,
 * 결제 승인,
 * 결제 취소
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentApiController {

    private final ManagePaymentUseCase managePaymentUseCase;

    @PostMapping("/{paymentGateway}")
    public ResponseEntity<PaymentRequestionDto> requestPayment(@AuthenticationPrincipal String loginId,
                                                               @PathVariable PaymentGateway paymentGateway,
                                                               @Valid @RequestBody List<ItemPayRequestDto> itemPayRequestDtos,
                                                               @RequestParam Long orderId,
                                                               HttpServletRequest request) {
        String requestURL = getRequestURL(request);

        PaymentRequestResult paymentReady = (PaymentRequestResult) managePaymentUseCase.createPaymentReady(
                itemPayRequestDtos,
                paymentGateway,
                requestURL,
                loginId,
                orderId);

        PaymentRequestionDto paymentRequestionDto = new PaymentRequestionDto(
                paymentReady.getRedirectUrl(),
                paymentReady.getPaymentId());

        return ResponseEntity.ok(paymentRequestionDto);
    }

    @PostMapping("/{paymentId}/approval/{paymentGateway}")
    public ResponseEntity<Void> approvalPaymentRequest(@AuthenticationPrincipal String loginId,
                                                       @PathVariable Long paymentId,
                                                       @PathVariable PaymentGateway paymentGateway,
                                                       @RequestParam("pg_token") String payToken) {
        managePaymentUseCase.approvePayment(paymentGateway, payToken, paymentId, loginId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{paymentId}/cancellation/{paymentGateway}")
    public ResponseEntity<Void> cancelPaymentRequest(@AuthenticationPrincipal String loginId,
                                                     @PathVariable Long paymentId,
                                                     @PathVariable PaymentGateway paymentGateway,
                                                     @Valid @RequestBody PayCancelRequestDto payCancelRequestDto) {
        managePaymentUseCase.cancelPayment(paymentGateway, payCancelRequestDto, paymentId, loginId);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/canceled")
    public ResponseEntity<String> redirectPayCanceled() {
        return ResponseEntity.internalServerError().body(CANCELED_PAY.getMessage());
    }

    @GetMapping("/failed")
    public ResponseEntity<String> redirectPayFailed() {
        return ResponseEntity.internalServerError().body(FAILED_PAY.getMessage());
    }

    private String getRequestURL(HttpServletRequest request) {
        return request.getRequestURL().toString().replace(request.getRequestURI(), "");
    }

}
