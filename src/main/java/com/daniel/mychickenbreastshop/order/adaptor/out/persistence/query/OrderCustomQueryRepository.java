package com.daniel.mychickenbreastshop.order.adaptor.out.persistence.query;

import com.daniel.mychickenbreastshop.order.domain.Order;
import com.daniel.mychickenbreastshop.order.domain.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderCustomQueryRepository {

    Page<Order> findAllByUserId(Long userId, OrderStatus orderStatus, Pageable pageable);

}
