package com.daniel.mychickenbreastshop.domain.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "SELECT o" +
            " FROM Order o" +
            " JOIN FETCH o.orderedProducts op" +
            " JOIN FETCH o.payment p" +
            " JOIN FETCH p.")
    List<Order> findByUserIdUsingFetchJoin(Long userId);
}
