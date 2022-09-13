package com.daniel.mychickenbreastshop.domain.payment.application;

import com.daniel.mychickenbreastshop.domain.order.domain.dto.request.ItemOrderRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PayService {

    @Transactional
    public String getSingleItemPayURL(ItemOrderRequestDto itemOrderRequestDto, String requestURL, Long userId) {
    }

    @Transactional
    public String getCartItemsPayURL(String value, String requestURL, Long userId) {
    }
}
