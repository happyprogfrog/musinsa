package com.musinsa.shop.domain.model;

public class BrandFixtures {
    public static Brand brand() {
        return new Brand(5L, "musinsastandard", "무신사 스탠다드");
    }

    public static Brand brand(Long id) {
        return new Brand(id, "alias" + id, "name" + id);
    }
}
