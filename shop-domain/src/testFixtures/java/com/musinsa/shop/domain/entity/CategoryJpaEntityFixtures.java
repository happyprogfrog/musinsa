package com.musinsa.shop.domain.entity;

import com.musinsa.shop.domain.enums.CategoryCode;

public class CategoryJpaEntityFixtures {
    public static CategoryJpaEntity entity() {
        return new CategoryJpaEntity(CategoryCode.TOP);
    }
}
