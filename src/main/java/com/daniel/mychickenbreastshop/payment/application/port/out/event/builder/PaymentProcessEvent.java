package com.daniel.mychickenbreastshop.payment.application.port.out.event.builder;

import com.daniel.mychickenbreastshop.global.event.model.EventModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentProcessEvent implements EventModel {

    private String eventType;
    private String payload;
    private String eventAction;

    @Override
    public String getEventType() {
        return eventType;
    }

    @Override
    public String getPayload() {
        return payload;
    }

    @Override
    public Optional<String> getEventAction() {
        return Optional.ofNullable(eventAction);
    }
}
