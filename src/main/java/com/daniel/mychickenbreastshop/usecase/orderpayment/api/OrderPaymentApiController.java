package com.daniel.mychickenbreastshop.usecase.orderpayment.api;

import com.daniel.mychickenbreastshop.usecase.orderpayment.application.crew.PaymentApplicationCrew;
import com.daniel.mychickenbreastshop.usecase.orderpayment.model.dto.request.ItemPayRequestDto;
import com.daniel.mychickenbreastshop.usecase.orderpayment.model.dto.request.PayCancelRequestDto;
import com.daniel.mychickenbreastshop.usecase.orderpayment.model.enums.PaymentGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.daniel.mychickenbreastshop.domain.payment.model.enums.ErrorMessages.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class OrderPaymentApiController {

    private final PaymentApplicationCrew paymentApplicationCrew;

    /**
     * 외부 결제 API 에 해당 결제 건의 데이터 요청 (고민)
     */
/*
    @GetMapping("/{paymentGateway}")
    public ResponseEntity<ApiPayInfoDto> getPayDetail(@PathVariable PaymentGateway paymentGateway,
                                                      @RequestParam String franchiseeId, @RequestParam String payId) {
        return ResponseEntity.ok(paymentApplicationCrew.getApiPaymentDetail(franchiseeId, payId, paymentGateway));
    }
*/

    /**
     * 외부 결제 API 에 결제 승인 요청
     */
    @GetMapping("/{paymentGateway}/completed")
    public ResponseEntity<Void> approvalRequest(@PathVariable PaymentGateway paymentGateway,
                                                @RequestParam("pg_token") String payToken,
                                                @RequestAttribute String loginId) {
        paymentApplicationCrew.approvePayment(payToken, loginId, paymentGateway);
        return ResponseEntity.ok().build();
    }

    /**
     * 외부 결제 시도 중 취소 시 얻은 Redirect Url 요청
     */
    @GetMapping("/canceled")
    public ResponseEntity<String> redirectPayCanceled() {
        return ResponseEntity.ok().body(CANCELED_PAY.getMessage());
    }


    /**
     * 외부 결제 시도 중 실패 시 얻은 Redirect Url 요청
     */
    @GetMapping("/failed")
    public ResponseEntity<String> redirectPayFailed() {
        return ResponseEntity.internalServerError().body(FAILED_PAY.getMessage());
    }

    /**
     * 결제 취소 요청
     */
    @PostMapping("/{paymentGateway}/cancel")
    public ResponseEntity<Void> cancelPayment(@PathVariable PaymentGateway paymentGateway,
                                              @Valid @RequestBody PayCancelRequestDto payCancelRequestDto,
                                              @RequestAttribute String loginId) {
        paymentApplicationCrew.cancelPayment(payCancelRequestDto, paymentGateway, loginId);
        return ResponseEntity.ok().build();
    }

    /**
     * 단일 상품 결제 요청
     */
    @PostMapping("/{paymentGateway}")
    public ResponseEntity<String> itemPay(@PathVariable PaymentGateway paymentGateway,
                                          @Valid @RequestBody ItemPayRequestDto itemPayRequestDto,
                                          @RequestAttribute String loginId,
                                          HttpServletRequest request) {
        String requestUrl = getRequestURL(request);
        String payableUrl = paymentApplicationCrew.getSingleItemPayUrl(itemPayRequestDto, requestUrl, loginId, paymentGateway);

        return ResponseEntity.ok(payableUrl);
    }

    /**
     * 장바구니 상품 결제 요청
     */
    @PostMapping("/cart/{paymentGateway}")
    public ResponseEntity<String> cartItemsPay(@PathVariable PaymentGateway paymentGateway,
                                               HttpServletRequest request,
                                               @CookieValue(value = "chicken") Cookie cookie,
                                               @RequestAttribute String loginId) {
        String requestUrl = getRequestURL(request);
        String payableUrl = paymentApplicationCrew.getCartItemsPayUrl(cookie.getValue(), requestUrl, loginId, paymentGateway);

        return ResponseEntity.ok(payableUrl);
    }

    private String getRequestURL(HttpServletRequest request) {
        return request.getRequestURL().toString().replace(request.getRequestURI(), "");
    }

}
