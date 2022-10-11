package com.daniel.mychickenbreastshop.domain.payment.model.order.query;

import com.daniel.mychickenbreastshop.domain.payment.model.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderCustomQueryRepository {

    Page<Order> findAllByUserId(Pageable pageable, Long userId);

    Optional<Order> findByIdWithFetchJoin(Long orderId);
}
