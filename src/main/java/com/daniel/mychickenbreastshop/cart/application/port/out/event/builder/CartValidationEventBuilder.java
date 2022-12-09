package com.daniel.mychickenbreastshop.cart.application.port.out.event.builder;

import com.daniel.mychickenbreastshop.cart.application.port.out.event.model.CartValidation;
import com.daniel.mychickenbreastshop.global.event.builder.EventBuilder;
import com.daniel.mychickenbreastshop.global.event.model.EventModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.stereotype.Component;

@Component
public class CartValidationEventBuilder implements EventBuilder<CartValidation> {

    private static final String EVENT_ACTION = "상품 유효성 검사";

    private final ObjectMapper objectMapper;

    public CartValidationEventBuilder(ObjectMapper objectMapper) {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        this.objectMapper = objectMapper;
    }

    @Override
    public EventModel createEvent(CartValidation domainEvent) {
        JsonNode jsonNode = objectMapper.convertValue(domainEvent, JsonNode.class);

        return CartValidationEvent.builder()
                .eventType(domainEvent.getClass().getSimpleName())
                .payload(jsonNode.toString())
                .eventAction(EVENT_ACTION)
                .build();
    }
}
