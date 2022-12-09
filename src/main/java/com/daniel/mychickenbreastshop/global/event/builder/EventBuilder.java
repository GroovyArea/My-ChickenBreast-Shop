package com.daniel.mychickenbreastshop.global.event.builder;

import com.daniel.mychickenbreastshop.global.event.model.EventModel;
import com.daniel.mychickenbreastshop.global.event.model.DomainEvent;

public interface EventBuilder<T extends DomainEvent> {

    EventModel createEvent(T domainEvent);
}
