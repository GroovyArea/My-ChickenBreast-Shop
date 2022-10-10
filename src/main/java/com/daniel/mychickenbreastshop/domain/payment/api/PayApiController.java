package com.daniel.mychickenbreastshop.domain.payment.api;

import com.daniel.mychickenbreastshop.domain.payment.application.payment.crew.PaymentApplicationCrew;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.request.ItemPayRequestDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.request.PayCancelRequestDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.dto.response.PaymentInfoResponseDto;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.model.PaymentApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
@RequestMapping("/api/v1/payments")
public class PayApiController {

    private final PaymentApplicationCrew paymentApplicationCrew;

    /**
     * 외부 결제 API 에 해당 결제 건의 데이터 요청 (고민 좀 해보자)
     */
/*    @GetMapping("/{paymentApi}")
    public ResponseEntity<ApiPayInfoDto> getPayDetail(@PathVariable PaymentApi paymentApi,
                                                      @RequestParam String franchiseeId, @RequestParam String payId) {
        return ResponseEntity.ok(payService.getApiPaymentDetail(franchiseeId, payId, paymentApi));
    }*/

    /**
     * 결제 정보(카드 정보) 조회
     */
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentInfoResponseDto> getPaymentInfo(@PathVariable Long paymentId) {
        PaymentInfoResponseDto paymentInfoResponseDto = paymentApplicationCrew.getPaymentDetail(paymentId);
        return ResponseEntity.ok(paymentInfoResponseDto);
    }

    /**
     * 외부 결제 API 에 결제 승인 요청
     */
    @GetMapping("/{paymentApi}/completed")
    public ResponseEntity<Void> approvalRequest(@PathVariable PaymentApi paymentApi,
                                                @RequestParam("pg_token") String payToken,
                                                HttpServletRequest request) {
        String loginId = getLoginId(request);
        paymentApplicationCrew.approvePayment(payToken, loginId, paymentApi);
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
    @PostMapping("/{paymentApi}/cancel")
    public ResponseEntity<Void> cancelPayment(@PathVariable PaymentApi paymentApi,
                                              @Valid @RequestBody PayCancelRequestDto payCancelRequestDto,
                                              HttpServletRequest request) {
        String loginId = getLoginId(request);
        paymentApplicationCrew.cancelPayment(payCancelRequestDto, paymentApi, loginId);
        return ResponseEntity.ok().build();
    }

    /**
     * 단일 상품 결제 요청
     */
    @PostMapping("/{paymentApi}")
    public ResponseEntity<String> itemPay(@PathVariable PaymentApi paymentApi,
                                          @Valid @RequestBody ItemPayRequestDto itemPayRequestDto,
                                          HttpServletRequest request) {
        String loginId = getLoginId(request);
        String requestUrl = getRequestURL(request);
        String payableUrl = paymentApplicationCrew.getSingleItemPayResultUrl(itemPayRequestDto, requestUrl, loginId, paymentApi);

        return ResponseEntity.ok(payableUrl);
    }

    /**
     * 장바구니 상품 결제 요청
     */
    @PostMapping("/cart/{paymentApi}")
    public ResponseEntity<String> cartItemsPay(@PathVariable PaymentApi paymentApi,
                                               HttpServletRequest request,
                                               @CookieValue(value = "chicken") Cookie cookie) {
        String loginId = getLoginId(request);
        String requestUrl = getRequestURL(request);
        String payableUrl = paymentApplicationCrew.getCartItemsPayUrl(cookie.getValue(), requestUrl, loginId, paymentApi);

        return ResponseEntity.ok(payableUrl);
    }


    private String getRequestURL(HttpServletRequest request) {
        return request.getRequestURL().toString().replace(request.getRequestURI(), "");
    }

    private String getLoginId(HttpServletRequest request) {
        return (String) request.getAttribute("loginId");
    }

}
