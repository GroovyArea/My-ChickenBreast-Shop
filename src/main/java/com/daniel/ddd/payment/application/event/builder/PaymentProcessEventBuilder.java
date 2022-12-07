package com.daniel.ddd.payment.application.event.builder;

public interface PaymentProcessEventBuilder<T> {

    PaymentProcessEvent createEvent(T domainEvent);
}
