package com.musinsa.shop.dashboard.controller.consts;

public enum ErrorCodes {
    // 공통 예외
    INVALID_REQUEST("유효하지 않은 요청입니다", "ERR-COMMON-001"),

    // Brand 관련 예외
    BRAND_NOT_FOUND("브랜드가 존재하지 않습니다", "ERR-BRAND-001"),
    DUPLICATE_ALIAS("브랜드의 별칭이 중복됩니다", "ERR-BRAND-001"),

    // Category 관련 예외
    CATEGORY_NOT_FOUND("카테고리가 존재하지 않습니다", "ERR-CATEGORY-001"),

    // Product 관련 예외
    PRODUCT_NOT_FOUND("제품이 존재하지 않습니다", "ERR-PRODUCT-001"),
    INVALID_PRICE("제품 가격이 유효하지 않습니다", "ERR-PRODUCT-002");

    public final String message;
    public final String code;

    ErrorCodes(String message, String code) {
        this.message = message;
        this.code = code;
    }
}
