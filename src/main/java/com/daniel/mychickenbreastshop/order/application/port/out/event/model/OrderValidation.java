package com.daniel.mychickenbreastshop.order.application.port.out.event.model;

import com.daniel.mychickenbreastshop.global.event.model.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class OrderValidation implements DomainEvent {

    private long itemNo;
    private int itemQuantity;
    private long totalPrice;

}
