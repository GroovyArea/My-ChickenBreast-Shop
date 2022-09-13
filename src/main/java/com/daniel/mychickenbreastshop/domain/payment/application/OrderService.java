package com.daniel.mychickenbreastshop.domain.payment.application;

import com.daniel.mychickenbreastshop.domain.payment.domain.order.OrderRepository;
import com.daniel.mychickenbreastshop.domain.payment.domain.order.dto.response.OrderDetailResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;

    public List<OrderDetailResponseDto> getOrderInfoData(Long userId, Pageable pageable) {
        return orderRepository.findByUserIdUsingFetchJoin(userId, pageable).stream()
                .map();
    }


    @Transactional
    public void cancelOrder(Long orderId, Long userId) {
    }
}
