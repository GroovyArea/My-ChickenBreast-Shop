package com.daniel.ddd.payment.application.service.gateway.kakaopay.error;

public class KakaoPayException extends RuntimeException{

    public KakaoPayException() {
        super();
    }

    public KakaoPayException(String message) {
        super(message);
    }

    public KakaoPayException(String message, Throwable cause) {
        super(message, cause);
    }

    public KakaoPayException(Throwable cause) {
        super(cause);
    }
}
