package com.daniel.mychickenbreastshop.domain.order.model.query;

import com.daniel.mychickenbreastshop.domain.order.model.Order;
import com.daniel.mychickenbreastshop.domain.order.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderCustomQueryRepository {

    Page<Order> findAllByUserId(Long userId, OrderStatus orderStatus, Pageable pageable);

    Optional<Order> findByIdWithFetchJoin(Long orderId);
}
