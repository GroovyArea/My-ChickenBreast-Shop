package com.daniel.ddd.order.adaptor.out.persistence;

import com.daniel.ddd.order.adaptor.out.persistence.query.OrderCustomQueryRepository;
import com.daniel.ddd.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderCustomQueryRepository {

    @Query(value = "select o" +
            " from Order o" +
            " join fetch OrderProduct op" +
            " on o.id = op.order.id" +
            " where o.id = :orderId")
    Optional<Order> findByIdWithOrderProductsUsingFetchJoin(@Param(value = "orderId") Long orderId);
}
