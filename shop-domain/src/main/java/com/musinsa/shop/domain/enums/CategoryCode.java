package com.musinsa.shop.domain.enums;

import java.util.Arrays;

public enum CategoryCode {
    TOP("001", "상의"),
    OUTER("002", "아우터"),
    PANTS("003", "바지"),
    SNEAKERS("004", "스니커즈"),
    BAG("005", "가방"),
    HAT("006", "모자"),
    SOCKS("007", "양말"),
    ACCESSORY("008", "악세서리");

    private final String code;
    private final String name;

    CategoryCode(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static CategoryCode fromCode(String code) {
        return Arrays.stream(CategoryCode.values())
                .filter(categoryCode -> categoryCode.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 카테고리 코드: " + code));
    }
}
