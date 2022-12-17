package com.daniel.mychickenbreastshop.product.item.adaptor.in.event.handler;

import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import com.daniel.mychickenbreastshop.global.error.exception.InternalErrorException;
import com.daniel.mychickenbreastshop.global.event.exception.EventNotExistsException;
import com.daniel.mychickenbreastshop.global.event.model.EventModel;
import com.daniel.mychickenbreastshop.global.util.JsonUtil;
import com.daniel.mychickenbreastshop.product.item.adaptor.out.persistence.ProductRepository;
import com.daniel.mychickenbreastshop.product.item.domain.Product;
import com.daniel.mychickenbreastshop.product.item.domain.enums.ErrorMessages;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemVariationEventHandler {

    private static final String ITEM_VARIATION_EVENT = "상품 수량 변경";

    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    @EventListener
    public void doItemVariationEvent(EventModel eventModel) {
        String eventAction = eventModel.getEventAction()
                .orElseThrow(EventNotExistsException::new);


        if (eventAction.equals(ITEM_VARIATION_EVENT)) {
            String payload = eventModel.getPayload();

            try {
                JsonNode jsonNode = objectMapper.readTree(payload);

                String jsonNumbers = JsonUtil.objectToString(jsonNode.path("numbers"));
                List<Long> numbers = objectMapper.readValue(jsonNumbers, new TypeReference<>() {
                });

                String jsonQuantities = JsonUtil.objectToString(jsonNode.path("quantities"));
                List<Integer> quantities = objectMapper.readValue(jsonQuantities, new TypeReference<>() {
                });

                long totalAmount = jsonNode.get("total_amount").asLong();
                boolean status = jsonNode.get("status").asBoolean();

                List<Product> products = numbers.stream()
                        .map(number -> productRepository.findById(number)
                                .orElseThrow(() -> new BadRequestException(ErrorMessages.ITEM_NOT_EXISTS.getMessage())))
                        .toList();

                validateItems(products, numbers, quantities, totalAmount);

                if (status) {
                    for (int i = 0; i < products.size(); i++) {
                        products.get(i).decreaseItemQuantity(quantities.get(i));
                    }
                } else {
                    for (int i = 0; i < products.size(); i++) {
                        products.get(i).increaseItemQuantity(quantities.get(i));
                    }
                }

            } catch (JsonProcessingException e) {
                throw new InternalErrorException(e.getMessage(), e);
            }
        }
    }

    private void validateItems(List<Product> products, List<Long> numbers, List<Integer> quantities, long totalAmount) {
        long sum = 0;

        for (int i = 0; i < numbers.size(); i++) {
            Product product = products.get(i);
            int price = product.getPrice();
            int quantity = quantities.get(i);

            sum += (long) price * quantity;
        }

        if (sum != totalAmount) {
            throw new BadRequestException(ErrorMessages.INVALID_PAY_AMOUNT.getMessage());
        }

    }
}
