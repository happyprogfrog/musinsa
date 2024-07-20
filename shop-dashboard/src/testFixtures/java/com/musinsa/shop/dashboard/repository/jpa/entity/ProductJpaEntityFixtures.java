package com.musinsa.shop.dashboard.repository.jpa.entity;

public class ProductJpaEntityFixtures {
    public static ProductJpaEntity entity() {
        final var brand = BrandJpaEntityFixtures.entity();
        final var category = CategoryJpaEntityFixtures.entity();

        return new ProductJpaEntity(
                brand, category, "name", 1000L
        );
    }

    public static ProductJpaEntity entity(Long index) {
        final var brand = BrandJpaEntityFixtures.entity();
        final var category = CategoryJpaEntityFixtures.entity();

        return new ProductJpaEntity(
                brand, category, "name" + index, 1000 * index
        );
    }
}
