package com.daniel.ddd.product.item.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessages {

    ITEM_NOT_EXISTS("상품이 존재하지 않습니다."),
    INVALID_PAY_AMOUNT("상품 총 가격이 잘못 되었습니다."),
    ITEM_QUANTITY_NOT_ENOUGH("상품 재고 수량이 불충분합니다."),
    FILE_UPLOAD_FAILED("파일 업로드에 실패했습니다."),
    CONTENT_TYPE_NOT_FOUND("업로드 가능한 파일은 이미지 형식이어야 합니다."),
    INVALID_FILE_CONTENT_TYPE("업로드 가능한 파일은 이미지 형식이어야 합니다.");

    private final String message;

}
