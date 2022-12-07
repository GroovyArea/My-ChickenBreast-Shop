package com.daniel.ddd.order.model.dto.response;

import com.daniel.ddd.order.domain.enums.OrderStatus;
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

    private Long orderId;
    private Integer totalCount;
    private Long orderPrice;
    private OrderStatus orderStatus;
    private LocalDateTime createAt;

}