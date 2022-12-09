package com.daniel.mychickenbreastshop.order.adaptor.in.event.handler;

import com.daniel.mychickenbreastshop.global.error.exception.InternalErrorException;
import com.daniel.mychickenbreastshop.global.event.exception.EventNotExistsException;
import com.daniel.mychickenbreastshop.global.event.model.EventModel;
import com.daniel.mychickenbreastshop.order.adaptor.out.persistence.OrderRepository;
import com.daniel.mychickenbreastshop.order.domain.Order;
import com.daniel.mychickenbreastshop.order.domain.enums.ErrorMessages;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventHandler {

    private static final String ORDER_EVENT = "주문";

    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    @EventListener
    public void doOrderEvent(EventModel eventModel) {
        String eventAction = eventModel.getEventAction()
                .orElseThrow(EventNotExistsException::new);

        if (eventAction.equals(ORDER_EVENT)) {
            String payload = eventModel.getPayload();

            try {
                JsonNode jsonNode = objectMapper.readTree(payload);
                long orderId = jsonNode.get("order_id").asLong();
                long paymentId = jsonNode.get("payment_id").asLong();
                boolean status = jsonNode.get("status").asBoolean();

                Order order = orderRepository.findById(orderId)
                        .orElseThrow(() -> new InternalErrorException(ErrorMessages.ORDER_NOT_EXISTS.getMessage()));

                order.updatePaymentInfo(paymentId);

                if (status) {
                    order.completeOrder();
                } else {
                    order.cancelOrder();
                }

            } catch (JsonProcessingException e) {
                throw new InternalErrorException(e.getMessage(), e);
            }
        }
    }
}
