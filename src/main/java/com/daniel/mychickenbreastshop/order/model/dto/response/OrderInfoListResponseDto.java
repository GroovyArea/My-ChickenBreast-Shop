package com.daniel.mychickenbreastshop.order.model.dto.response;

import com.daniel.mychickenbreastshop.order.domain.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderInfoListResponseDto {

    private long orderId;
    private long paymentId;
    private int totalCount;
    private long orderPrice;
    private OrderStatus orderStatus;
    private LocalDateTime createAt;

}
