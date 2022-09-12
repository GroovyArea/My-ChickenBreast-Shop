package com.daniel.mychickenbreastshop.domain.order.application;

import com.daniel.mychickenbreastshop.domain.order.domain.OrderRepository;
import com.daniel.mychickenbreastshop.domain.order.domain.dto.request.ItemOrderRequestDto;
import com.daniel.mychickenbreastshop.domain.order.domain.dto.response.OrderDetailResponseDto;
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
    public String getSingleItemPayURL(ItemOrderRequestDto itemOrderRequestDto, String requestURL) {


    }

    @Transactional
    public String getCartItemsPayURL(String value, String requestURL) {
    }

    @Transactional
    public void cancelOrder(Long orderId) {
    }
}
