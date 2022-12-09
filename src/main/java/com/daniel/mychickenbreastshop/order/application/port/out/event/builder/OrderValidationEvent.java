package com.daniel.mychickenbreastshop.order.application.port.out.event.builder;

import com.daniel.mychickenbreastshop.global.event.model.EventModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderValidationEvent implements EventModel {

    private String eventType;
    private String payload;
    private String eventAction;
}
