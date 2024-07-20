package com.musinsa.shop.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum CategoryCode {
    TOP("001", "상의"),
    OUTER("002", "아우터"),
    PANTS("003", "바지"),
    SNEAKERS("004", "스니커즈"),
    BAG("005", "가방"),
    HAT("006", "모자"),
    SOCKS("007", "양말"),
    ACCESSORY("008", "악세서리");

    private final String key;
    private final String value;

    public static CategoryCode fromKey(String categoryCodeKey) {
        return Arrays.stream(CategoryCode.values())
                .filter(categoryCode -> categoryCode.getKey().equals(categoryCodeKey))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 카테고리 코드 key: " + categoryCodeKey));
    }
}
