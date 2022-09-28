package com.daniel.mychickenbreastshop.domain.payment.domain.order.query;

import com.daniel.mychickenbreastshop.domain.payment.domain.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderCustomQueryRepository {

    Page<Order> findAllByUserId(Pageable pageable, Long userId);

}
