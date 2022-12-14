package com.daniel.mychickenbreastshop.payment.adaptor.out.persistence.query;

import com.daniel.mychickenbreastshop.payment.domain.Payment;
import com.daniel.mychickenbreastshop.payment.domain.QPayment;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.daniel.mychickenbreastshop.payment.domain.QCard.card;
import static com.daniel.mychickenbreastshop.payment.domain.QPayment.payment;


@Repository
@RequiredArgsConstructor
public class PaymentCustomQueryRepositoryImpl implements PaymentCustomQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Payment> findByIdWithCardUsingFetchJoin(Long paymentId) {
        Payment payment = queryFactory.selectFrom(QPayment.payment)
                .join(QPayment.payment.card, card)
                .fetchJoin()
                .where(paymentIdEq(paymentId))
                .fetchOne();

        return Optional.ofNullable(payment);
    }

    private BooleanExpression paymentIdEq(Long paymentIdCond) {
        return paymentIdCond != null ? payment.id.eq(paymentIdCond) : null;
    }
}
