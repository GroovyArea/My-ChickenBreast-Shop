package com.daniel.mychickenbreastshop.global.event.model;

import java.util.Optional;

public interface EventModel {

    String getEventType();
    String getPayload();
    Optional<String> getEventAction();
}
