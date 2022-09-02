package com.daniel.mychickenbreastshop.domain.order.domain.dto.response;

import com.daniel.mychickenbreastshop.domain.order.domain.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class OrderResponseDto {

    private String id;
    private Integer orderPrice;
    private Integer totalCount;
    private OrderStatus status;
    private OrderedProductResponseDto orderedProductResponseDto;

}
