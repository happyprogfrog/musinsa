package com.musinsa.shop.dashboard.controller.consts;

public enum ErrorCodes {
    BRAND_NOT_FOUND("브랜드가 존재하지 않습니다", 1000L),
    DUPLICATE_ALIAS("브랜드의 별칭이 중복됩니다", 1001L);

    public final String message;
    public final Long code;

    ErrorCodes(String message, Long code) {
        this.message = message;
        this.code = code;
    }
}
