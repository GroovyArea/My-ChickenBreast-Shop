package com.daniel.mychickenbreastshop.order.application.port.out.event.builder;

import com.daniel.mychickenbreastshop.global.event.builder.EventBuilder;
import com.daniel.mychickenbreastshop.global.event.model.EventModel;
import com.daniel.mychickenbreastshop.order.application.port.out.event.model.OrderValidation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.stereotype.Component;

@Component
public class OrderValidationEventBuilder implements EventBuilder<OrderValidation> {

    private static final String EVENT_ACTION = "상품 유효성 검사";

    private final ObjectMapper objectMapper;

    public OrderValidationEventBuilder(ObjectMapper objectMapper) {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        this.objectMapper = objectMapper;
    }

    @Override
    public EventModel createEvent(OrderValidation domainEvent) {
        JsonNode jsonNode = objectMapper.convertValue(domainEvent, JsonNode.class);

        return OrderValidationEvent.builder()
                .eventType(domainEvent.getClass().getSimpleName())
                .payload(jsonNode.toString())
                .eventAction(EVENT_ACTION)
                .build();
    }
}
