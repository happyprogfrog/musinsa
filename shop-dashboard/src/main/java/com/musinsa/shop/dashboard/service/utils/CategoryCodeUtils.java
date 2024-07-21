package com.musinsa.shop.dashboard.service.utils;

import com.musinsa.shop.dashboard.service.exception.CategoryNotFoundException;
import com.musinsa.shop.domain.enums.CategoryCode;

public class CategoryCodeUtils {
    public static CategoryCode getCategoryCodeFromKey(String categoryCodeKey) {
        CategoryCode categoryCode;

        try {
            categoryCode = CategoryCode.fromKey(categoryCodeKey);
        } catch (IllegalArgumentException ex) {
            throw new CategoryNotFoundException();
        }

        return categoryCode;
    }
}
