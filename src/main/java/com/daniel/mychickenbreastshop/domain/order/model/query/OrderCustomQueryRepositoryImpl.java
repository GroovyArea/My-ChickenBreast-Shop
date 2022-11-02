package com.daniel.mychickenbreastshop.domain.order.model.query;

import com.daniel.mychickenbreastshop.domain.order.model.Order;
import com.daniel.mychickenbreastshop.domain.order.model.QOrder;
import com.daniel.mychickenbreastshop.domain.order.model.enums.OrderStatus;
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

import static com.daniel.mychickenbreastshop.domain.order.model.QOrder.order;
import static com.daniel.mychickenbreastshop.domain.payment.model.QPayment.payment;


@Repository
@RequiredArgsConstructor
public class OrderCustomQueryRepositoryImpl implements OrderCustomQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override //
    public Page<Order> findAllByUserId(Long userId, OrderStatus orderStatus, Pageable pageable) {
        List<Order> results = queryFactory.selectFrom(order)
                .where(userIdEq(userId), statusEq(orderStatus))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(order.createdAt.desc())
                .fetch();

        JPAQuery<Order> count = queryFactory.selectFrom(order)
                .where(userIdEq(userId));

        return PageableExecutionUtils.getPage(results, pageable, () -> count.fetch().size());

    }

    @Override
    public Optional<Order> findByIdWithFetchJoin(Long orderId) {
        Order order = queryFactory.selectFrom(QOrder.order)
                .join(QOrder.order.payment, payment)
                .fetchJoin()
                .where(orderIdEq(orderId))
                .fetchOne();

        return Optional.ofNullable(order);
    }

    private BooleanExpression userIdEq(Long userIdCond) {
        return userIdCond != null ? order.user.id.eq(userIdCond) : null;
    }

    private BooleanExpression orderIdEq(Long orderIdCond) {
        return orderIdCond != null ? order.id.eq(orderIdCond) : null;
    }

    private BooleanExpression statusEq(OrderStatus statusCond) {
        return statusCond != null ? order.status.eq(statusCond) : null;
    }

}
