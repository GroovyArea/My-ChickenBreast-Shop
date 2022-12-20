package com.daniel.mychickenbreastshop.order.adaptor.out.persistence.query;

import com.daniel.mychickenbreastshop.order.domain.Order;
import com.daniel.mychickenbreastshop.order.domain.enums.OrderStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.daniel.mychickenbreastshop.order.domain.QOrder.order;
import static com.daniel.mychickenbreastshop.order.domain.QOrderProduct.orderProduct;

@Repository
@RequiredArgsConstructor
public class OrderCustomQueryRepositoryImpl implements OrderCustomQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Order> findAllByUserId(Long userId, OrderStatus orderStatus, Pageable pageable) {
        List<Order> results = queryFactory.selectFrom(order)
                .join(orderProduct)
                .on(orderIdEq())
                .where(userIdEq(userId), statusEq(orderStatus))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(order.createdAt.desc())
                .fetch();

        JPAQuery<Order> count = queryFactory.selectFrom(order)
                .where(userIdEq(userId));

        return PageableExecutionUtils.getPage(results, pageable, () -> count.fetch().size());

    }

    private BooleanExpression orderIdEq() {
        return order.id.eq(orderProduct.order.id);
    }

    private BooleanExpression userIdEq(Long userIdCond) {
        return userIdCond != null ? order.userId.eq(userIdCond) : null;
    }

    private BooleanExpression statusEq(OrderStatus statusCond) {
        return statusCond != null ? order.status.eq(statusCond) : null;
    }

}
