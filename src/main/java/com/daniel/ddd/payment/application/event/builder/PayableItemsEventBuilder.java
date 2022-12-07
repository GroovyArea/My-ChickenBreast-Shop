package com.daniel.ddd.payment.application.event.builder;

import com.daniel.ddd.payment.application.event.model.ItemsVariation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.stereotype.Component;

@Component
public class PayableItemsEventBuilder implements PaymentProcessEventBuilder<ItemsVariation> {

    private static final String EVENT_ACTION = "상품 수량 변경";

    public PayableItemsEventBuilder(ObjectMapper objectMapper) {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        this.objectMapper = objectMapper;
    }

    private final ObjectMapper objectMapper;

    @Override
    public PaymentProcessEvent createEvent(ItemsVariation domainEvent) {
        JsonNode jsonNode = objectMapper.convertValue(domainEvent, JsonNode.class);

        return PaymentProcessEvent.builder()
                .eventType(domainEvent.getClass().getSimpleName())
                .payload(jsonNode.toString())
                .eventAction(EVENT_ACTION)
                .build();
    }
}
