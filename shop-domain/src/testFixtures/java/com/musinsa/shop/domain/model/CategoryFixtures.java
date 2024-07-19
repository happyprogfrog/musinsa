package com.musinsa.shop.domain.model;

import com.musinsa.shop.domain.enums.CategoryCode;

public class CategoryFixtures {
    public static Category categoryTop() {
        return new Category(1L, CategoryCode.TOP);
    }

    public static Category categoryOuter() {
        return new Category(2L, CategoryCode.OUTER);
    }
}
