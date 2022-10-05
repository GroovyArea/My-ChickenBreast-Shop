package com.daniel.mychickenbreastshop.domain.payment.domain.order;

import com.daniel.mychickenbreastshop.domain.payment.domain.order.query.OrderCustomQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderCustomQueryRepository {

/*    @Override
    @Query(value = "select o" +
            " from Order o" +
            " join fetch OrderProduct op" +
            " on o.id = op.order.id" +
            " where o.id = :orderId")
    Optional<Order> findById(@Param(value = "orderId") Long orderId);*/
}
