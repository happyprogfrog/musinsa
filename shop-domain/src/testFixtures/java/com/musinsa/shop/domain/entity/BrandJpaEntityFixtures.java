package com.musinsa.shop.domain.entity;

public class BrandJpaEntityFixtures {
    public static BrandJpaEntity entity() {
        return new BrandJpaEntity("alias", "name");
    }

    public static BrandJpaEntity entity(Long index) {
        return new BrandJpaEntity("alias" + index, "name" + index);
    }
}
