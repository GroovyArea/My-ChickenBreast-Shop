package com.daniel.mychickenbreastshop.domain.payment.domain.order;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "SELECT o" +
            " FROM Order o" +
            " JOIN FETCH o.orderProducts op" +
            " JOIN FETCH o.payment p" +
            " JOIN FETCH p.card c")
    List<Order> findByUserIdUsingFetchJoin(Long userId, Pageable pageable);

}
