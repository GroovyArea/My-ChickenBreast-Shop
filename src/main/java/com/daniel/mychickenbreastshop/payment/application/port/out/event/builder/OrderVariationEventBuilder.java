package com.daniel.mychickenbreastshop.payment.application.port.out.event.builder;

import com.daniel.mychickenbreastshop.global.event.builder.EventBuilder;
import com.daniel.mychickenbreastshop.payment.application.port.out.event.model.OrderVariation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.stereotype.Component;

@Component
public class OrderVariationEventBuilder implements EventBuilder<OrderVariation> {

    private static final String EVENT_ACTION = "주문";

    public OrderVariationEventBuilder(ObjectMapper objectMapper) {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        this.objectMapper = objectMapper;
    }

    private final ObjectMapper objectMapper;

    @Override
    public PaymentProcessEvent createEvent(OrderVariation domainEvent) {
        JsonNode jsonNode = objectMapper.convertValue(domainEvent, JsonNode.class);

        return PaymentProcessEvent.builder()
                .eventType(domainEvent.getClass().getSimpleName())
                .payload(jsonNode.toString())
                .eventAction(EVENT_ACTION)
                .build();
    }
}
