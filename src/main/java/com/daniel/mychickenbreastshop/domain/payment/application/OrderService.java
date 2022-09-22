package com.daniel.mychickenbreastshop.domain.payment.application;

import com.daniel.mychickenbreastshop.domain.payment.domain.order.OrderRepository;
import com.daniel.mychickenbreastshop.domain.payment.domain.order.dto.response.OrderProductResponseDto;
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

    /**
     * 디비 조회 로직 (무엇을 조인할지 고민 후 배치사이즈로 성능 최적화)
     * @param userId
     * @param pageable
     * @return
     */
    public List<OrderProductResponseDto> getOrderInfoData(Long userId, Pageable pageable) {
        return null;
    }

}
