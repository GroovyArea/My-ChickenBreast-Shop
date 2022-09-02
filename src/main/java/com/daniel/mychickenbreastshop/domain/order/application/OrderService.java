package com.daniel.mychickenbreastshop.domain.order.application;

import com.daniel.mychickenbreastshop.domain.order.domain.OrderRepository;
import com.daniel.mychickenbreastshop.domain.order.domain.dto.response.OrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public List<OrderResponseDto> getOrderInfoData(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map();
    }
}
