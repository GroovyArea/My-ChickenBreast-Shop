package com.daniel.ddd.order.application.port.in;

import com.daniel.ddd.order.model.dto.request.OrderRequestDto;

import java.util.List;

public interface ManageOrderUseCase {

    Long makeOrderReady(List<OrderRequestDto> orderRequestDtos, Long userId); // event publisher로 상품 수량 확인 및 예외 발생시키기

    void makeOrderComplete(Long orderId);

    void makeOrderCanceled(Long orderId);

}
