package com.daniel.mychickenbreastshop.domain.payment.api;

import com.daniel.mychickenbreastshop.domain.payment.application.PayService;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.request.ItemPayRequestDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.request.PayCancelRequestDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.response.PayApprovalResponseDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.response.kakaopay.ApiPayInfoDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.model.PaymentApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import static com.daniel.mychickenbreastshop.domain.payment.domain.pay.model.PaymentResponse.CANCELED_PAY;
import static com.daniel.mychickenbreastshop.domain.payment.domain.pay.model.PaymentResponse.FAILED_PAY;

/**
 * 결제 API
 * 외부 결제 API 사용
 * <pre>
 *     <b>history</b>
 *     1.0 2022.09.13 최초작성
 * </pre>
 *
 * @author Daniel Kim
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PayApiController {

    private final PayService payService;

    /**
     * 외부 결제 API 에 해당 결제 건의 데이터 요청 (고민 좀 해보자)
     */
    @GetMapping("/{paymentApi}")
    public ResponseEntity<ApiPayInfoDto> getPayDetail(@PathVariable PaymentApi paymentApi,
                                                      @RequestParam String franchiseeId, @RequestParam String payId) {
        return ResponseEntity.ok(payService.getApiPaymentDetail(franchiseeId, payId, paymentApi));
    }

    /**
     * 외부 결제 API 에 결제 승인 요청
     */
    @GetMapping("/{paymentApi}/completed")
    public ResponseEntity<PayApprovalResponseDto> approvalRequest(@PathVariable PaymentApi paymentApi,
                                                                  @RequestParam("pay_token") String payToken) {
        payService.approvePayment(payToken, paymentApi);
        return ResponseEntity.ok().build();
    }

    /**
     * 외부 결제 시도 중 취소 시 얻은 Redirect Url 요청
     */
    @GetMapping("/canceled")
    public ResponseEntity<String> redirectPayCanceled() {
        return ResponseEntity.badRequest().body(CANCELED_PAY.getMessage());
    }


    /**
     * 외부 결제 시도 중 실패 시 얻은 Redirect Url 로 요청
     */
    @GetMapping("/failed")
    public ResponseEntity<String> redirectPayFailed() {
        return ResponseEntity.badRequest().body(FAILED_PAY.getMessage());
    }

    /**
     * 결제 취소 요청
     */
    @PostMapping("/{paymentApi}/cancel")
    public ResponseEntity<Void> cancelPayment(@PathVariable PaymentApi paymentApi,
                                              @RequestBody PayCancelRequestDto payCancelRequestDto) {
        payService.cancelPayment(payCancelRequestDto, paymentApi);
        return ResponseEntity.ok().build();
    }

    /**
     * 단일 상품 결제 요청
     */
    @PostMapping("/{paymentApi}")
    public ResponseEntity<String> itemPay(@PathVariable PaymentApi paymentApi,
                                          @RequestBody ItemPayRequestDto itemPayRequestDto,
                                          HttpServletRequest request) {
        Long userId = getUserId(request);
        String requestURL = getRequestURL(request);
        String payableURL = payService.getSingleItemPayURL(itemPayRequestDto, requestURL, userId, paymentApi);

        return ResponseEntity.ok(payableURL);
    }

    /**
     * 장바구니 상품 결제 요청
     */
    @PostMapping("/{paymentApi}/cart")
    public ResponseEntity<String> cartItemsPay(@PathVariable PaymentApi paymentApi,
                                               HttpServletRequest request,
                                               @CookieValue(value = "chicken") Cookie cookie) {
        Long userId = getUserId(request);
        String requestURL = getRequestURL(request);
        String payableURL = payService.getCartItemsPayURL(cookie.getValue(), requestURL, userId, paymentApi);

        return ResponseEntity.ok(payableURL);
    }



    private String getRequestURL(HttpServletRequest request) {
        return request.getRequestURL().toString().replace(request.getRequestURI(), "");
    }

    private Long getUserId(HttpServletRequest request) {
        return (Long) request.getAttribute("userId");
    }

}
