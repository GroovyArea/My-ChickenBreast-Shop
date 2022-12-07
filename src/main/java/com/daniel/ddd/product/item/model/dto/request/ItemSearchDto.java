package com.daniel.ddd.product.item.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemSearchDto {

    private String searchKey; // 상품 번호, 상품 이름
    private String searchValue;
}
