package com.musinsa.shop.domain.model;

public class ProductFixtures {
    public static Product product() {
        var brand = BrandFixtures.brand();
        var category = CategoryFixtures.categoryTop();

        return new Product(10L, brand, category, "티셔츠", 12345L);
    }

    public static Product product(Long id) {
        var brand = BrandFixtures.brand(id);
        var category = CategoryFixtures.categoryOuter();
        return new Product(id, brand, category, "name" + id, 1000 * id);
    }
}
