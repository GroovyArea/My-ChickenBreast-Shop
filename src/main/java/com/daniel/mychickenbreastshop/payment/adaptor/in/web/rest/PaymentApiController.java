package com.daniel.mychickenbreastshop.payment.adaptor.in.web.rest;

import com.daniel.mychickenbreastshop.payment.adaptor.in.web.rest.dto.PaymentRequestionDto;
import com.daniel.mychickenbreastshop.payment.application.port.in.ManagePaymentUseCase;
import com.daniel.mychickenbreastshop.payment.application.service.gateway.model.PaymentRequestResult;
import com.daniel.mychickenbreastshop.payment.model.dto.request.ItemPayRequestDto;
import com.daniel.mychickenbreastshop.payment.model.dto.request.PayCancelRequestDto;
import com.daniel.mychickenbreastshop.payment.application.service.gateway.model.enums.PaymentGateway;
import com.daniel.mychickenbreastshop.user.auth.security.model.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final ManagePaymentUseCase paymentUseCase;

    @PostMapping("/{paymentGateway}")
    public ResponseEntity<PaymentRequestionDto> requestPayment(@PathVariable PaymentGateway paymentGateway,
                                                               @Valid @RequestBody List<ItemPayRequestDto> itemPayRequestDtos,
                                                               @RequestParam Long orderId,
                                                               HttpServletRequest request) {
        String requestURL = getRequestURL(request);
        String currentLoginId = getCurrentLoginId();

        PaymentRequestResult paymentReady = (PaymentRequestResult) paymentUseCase.createPaymentReady(
                itemPayRequestDtos,
                paymentGateway,
                requestURL,
                currentLoginId,
                orderId);

        PaymentRequestionDto paymentRequestionDto = new PaymentRequestionDto(
                paymentReady.getRedirectUrl(),
                paymentReady.getPaymentId());

        return ResponseEntity.ok(paymentRequestionDto);
    }

    @PostMapping("/{paymentId}/approval/{paymentGateway}")
    public ResponseEntity<Void> approvalPaymentRequest(@PathVariable Long paymentId,
                                                       @PathVariable PaymentGateway paymentGateway,
                                                       @RequestParam("pg_token") String payToken) {
        paymentUseCase.approvePayment(paymentGateway, payToken, paymentId, getCurrentLoginId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{paymentId}/cancellation/{paymentGateway}")
    public ResponseEntity<Void> cancelPaymentRequest(@PathVariable Long paymentId,
                                                     @PathVariable PaymentGateway paymentGateway,
                                                     @Valid @RequestBody PayCancelRequestDto payCancelRequestDto) {
        paymentUseCase.cancelPayment(paymentGateway, payCancelRequestDto, paymentId, getCurrentLoginId());
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

    private String getCurrentLoginId() {
        PrincipalDetails principal = (PrincipalDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return principal.getLoginId();
    }
}
