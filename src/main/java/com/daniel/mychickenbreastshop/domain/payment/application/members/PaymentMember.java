package com.daniel.mychickenbreastshop.domain.payment.application.members;

import com.daniel.mychickenbreastshop.domain.payment.application.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentMember {

    private final PaymentService paymentService;

    /**
     * api에서 요청하는 메서드들 만들기 payservice 참고
     * 트랜잭션 어디서 처리할지 고민해보자
     * 클래스 이름도
     */
}
