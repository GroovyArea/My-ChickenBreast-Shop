package com.daniel.mychickenbreastshop.usecase.orderpayment.application.strategy.service.kakaopay;

import com.daniel.mychickenbreastshop.domain.product.item.model.Product;
import com.daniel.mychickenbreastshop.domain.product.item.model.ProductRepository;
import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import com.daniel.mychickenbreastshop.usecase.orderpayment.application.strategy.service.adjust.ItemQuantityAdjuster;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static com.daniel.mychickenbreastshop.domain.product.item.model.enums.ProductResponse.ITEM_NOT_EXISTS;

/**
 * 카카오페이 결제 상품
 * 재고량 조절기
 */
@Component
@RequiredArgsConstructor
public class KakaopayItemQuantityAdjuster implements ItemQuantityAdjuster {

    private final ProductRepository productRepository;

    @Override
    public void quantityIncrease(String itemCode, String itemName, int quantity) {
        if (itemCode.isEmpty()) { // 다중 상품 결제 시
            String[] itemCodes = getItemCodes(itemCode);
            List<String> itemNumbers = getItemNumbers(itemCodes);
            List<String> itemQuantities = getItemQuantities(itemCodes);

            for (int i = 0; i < itemNumbers.size(); i++) {
                Product savedProduct = productRepository.findById(Long.valueOf(itemNumbers.get(i))).orElseThrow(() -> new BadRequestException(ITEM_NOT_EXISTS.getMessage()));
                savedProduct.increaseItemQuantity(Integer.parseInt(itemQuantities.get(i)));
            }
        } else { // 단일 상품 결제 시
            Product savedProduct = productRepository.findByName(itemName).orElseThrow(() -> new BadRequestException(ITEM_NOT_EXISTS.getMessage()));
            savedProduct.increaseItemQuantity(quantity);
        }
    }

    @Override
    public void quantityDecrease(String itemCode, String itemName, int quantity) {
        if (!itemCode.isEmpty()) { // 다중 상품 결제 시
            String[] itemCodes = getItemCodes(itemCode);
            List<String> itemNumbers = getItemNumbers(itemCodes);
            List<String> itemQuantities = getItemQuantities(itemCodes);

            for (int i = 0; i < itemNumbers.size(); i++) {
                Product savedProduct = productRepository.findById(Long.valueOf(itemNumbers.get(i))).orElseThrow(() -> new BadRequestException(ITEM_NOT_EXISTS.getMessage()));
                savedProduct.decreaseItemQuantity(Integer.parseInt(itemQuantities.get(i)));
            }
        } else { // 단일 상품 결제 시
            Product savedProduct = productRepository.findByName(itemName).orElseThrow(() -> new BadRequestException(ITEM_NOT_EXISTS.getMessage()));
            savedProduct.decreaseItemQuantity(quantity);
        }
    }

    private String[] getItemCodes(String itemCode) {
        return itemCode.split("/");
    }

    private List<String> getItemQuantities(String[] itemCodes) {
        return Arrays.stream(itemCodes[1].split(",")).toList();
    }

    private List<String> getItemNumbers(String[] itemCodes) {
        return Arrays.stream(itemCodes[0].split(",")).toList();
    }

}
