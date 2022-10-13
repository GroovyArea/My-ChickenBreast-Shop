package com.daniel.mychickenbreastshop.usecase.orderpayment.application.gateway.kakaopay.error;

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
