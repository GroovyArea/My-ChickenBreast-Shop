package com.daniel.mychickenbreastshop.product.item.adaptor.in.event;

import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import com.daniel.mychickenbreastshop.global.error.exception.InternalErrorException;
import com.daniel.mychickenbreastshop.global.event.exception.EventNotExistsException;
import com.daniel.mychickenbreastshop.global.event.model.EventModel;
import com.daniel.mychickenbreastshop.product.item.adaptor.out.persistence.ProductRepository;
import com.daniel.mychickenbreastshop.product.item.domain.Product;
import com.daniel.mychickenbreastshop.product.item.domain.enums.ErrorMessages;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemValidationEventHandler {

    private static final String ITEM_VALIDATION_EVENT = "상품 유효성 검사";

    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    @EventListener
    public void doItemValidationEvent(EventModel eventModel) {
        String eventAction = eventModel.getEventAction()
                .orElseThrow(EventNotExistsException::new);

        if (eventAction.equals(ITEM_VALIDATION_EVENT)) {
            String payload = eventModel.getPayload();

            try {
                JsonNode jsonNode = objectMapper.readTree(payload);
                long itemNo = jsonNode.get("item_no").asLong();
                int itemQuantity = jsonNode.get("item_quantity").asInt();
                long totalPrice = jsonNode.get("total_price").asLong();

                Product product = productRepository.findById(itemNo)
                        .orElseThrow(() -> new BadRequestException(ErrorMessages.ITEM_NOT_EXISTS.getMessage()));

                product.checkStockQuantity(itemQuantity);

                if ((long) itemQuantity * product.getPrice() != totalPrice) {
                    throw new BadRequestException(ErrorMessages.INVALID_PAY_AMOUNT.getMessage());
                }

            } catch (JsonProcessingException e) {
                throw new InternalErrorException(e.getMessage(), e);
            }
        }
    }
}
