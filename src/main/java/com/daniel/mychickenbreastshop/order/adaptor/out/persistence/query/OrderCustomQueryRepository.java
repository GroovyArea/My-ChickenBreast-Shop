package com.daniel.mychickenbreastshop.order.adaptor.out.persistence.query;

import com.daniel.mychickenbreastshop.order.domain.Order;
import com.daniel.mychickenbreastshop.order.domain.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderCustomQueryRepository {

    Page<Order> findAllByUserId(Long userId, OrderStatus orderStatus, Pageable pageable);

    Optional<Order> findByIdWithPaymentUsingFetchJoin(Long orderId);
}
