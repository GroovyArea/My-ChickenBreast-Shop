package com.daniel.mychickenbreastshop.usecase.orderpayment.application.strategy.service.adjust;

/**
 * 재고량 조절기
 */
public interface ItemQuantityAdjuster {

    void quantityIncrease(String itemCode, String itemName, int quantity);

    void quantityDecrease(String itemCode, String itemName, int quantity);
}
