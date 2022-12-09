package com.daniel.mychickenbreastshop.payment.application.port.out.event.builder;

import com.daniel.mychickenbreastshop.global.event.builder.EventBuilder;
import com.daniel.mychickenbreastshop.global.event.model.EventModel;
import com.daniel.mychickenbreastshop.payment.application.port.out.event.model.ItemsVariation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.stereotype.Component;

@Component
public class ItemsVariationEventBuilder implements EventBuilder<ItemsVariation> {

    private static final String EVENT_ACTION = "상품 수량 변경";

    private final ObjectMapper objectMapper;

    public ItemsVariationEventBuilder(ObjectMapper objectMapper) {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        this.objectMapper = objectMapper;
    }

    @Override
    public EventModel createEvent(ItemsVariation domainEvent) {
        JsonNode jsonNode = objectMapper.convertValue(domainEvent, JsonNode.class);

        return PaymentProcessEvent.builder()
                .eventType(domainEvent.getClass().getSimpleName())
                .payload(jsonNode.toString())
                .eventAction(EVENT_ACTION)
                .build();
    }
}
