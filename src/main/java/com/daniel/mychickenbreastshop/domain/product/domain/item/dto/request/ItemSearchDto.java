package com.daniel.mychickenbreastshop.domain.product.domain.item.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemSearchDto {

    private String searchKey; // 상품 이름
    private String searchValue;
}
