package com.daniel.mychickenbreastshop.cart.application.service;

import com.daniel.mychickenbreastshop.cart.application.port.out.event.model.CartValidation;
import com.daniel.mychickenbreastshop.cart.model.dto.request.CartRequestDto;
import com.daniel.mychickenbreastshop.global.event.builder.EventBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartValidator {

    private final ApplicationEventPublisher eventPublisher;
    private final EventBuilder<CartValidation> eventBuilder;

    public void cartValidate(CartRequestDto cartRequestDto) {
        eventPublisher.publishEvent(
                eventBuilder.createEvent(new CartValidation(
                        cartRequestDto.getItemNo(),
                        cartRequestDto.getItemQuantity(),
                        cartRequestDto.getTotalPrice()
                ))
        );
    }
}
