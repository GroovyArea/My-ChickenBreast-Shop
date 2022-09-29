package com.daniel.mychickenbreastshop.domain.payment.domain.order.query;

import com.daniel.mychickenbreastshop.domain.payment.domain.order.Order;
import com.daniel.mychickenbreastshop.domain.payment.domain.order.QOrder;
import com.daniel.mychickenbreastshop.domain.payment.domain.pay.QCard;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.daniel.mychickenbreastshop.domain.payment.domain.order.QOrder.order;
import static com.daniel.mychickenbreastshop.domain.payment.domain.pay.QPayment.payment;

@Repository
@RequiredArgsConstructor
public class OrderCustomQueryRepositoryImpl implements OrderCustomQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Order> findAllByUserId(Pageable pageable, Long userId) {
        List<Order> results = queryFactory.selectFrom(order)
                .where(userIdEq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Order> count = queryFactory.selectFrom(order)
                .where(userIdEq(userId));

        return PageableExecutionUtils.getPage(results, pageable, () -> count.fetch().size());

    }

    @Override
    public Optional<Order> findByIdWithFetchJoin(Long orderId) {
        Order order = queryFactory.selectFrom(QOrder.order)
                .join(QOrder.order.payment, payment)
                .fetchJoin().on(QOrder.order.id.eq(payment.order.id))
                .where(orderIdEq(orderId))
                .join(QOrder.order.payment.card, QCard.card).fetchJoin()
                .on(QOrder.order.payment.id.eq(QCard.card.payment.id))
                .fetchOne();

        return Optional.ofNullable(order);
    }

    private BooleanExpression userIdEq(Long userIdCond) {
        return userIdCond != null ? order.user.id.eq(userIdCond) : null;
    }

    private BooleanExpression orderIdEq(Long orderIdCond) {
        return orderIdCond != null ? order.id.eq(orderIdCond) : null;
    }

}
