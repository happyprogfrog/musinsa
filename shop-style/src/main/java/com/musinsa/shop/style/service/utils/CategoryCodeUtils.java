package com.musinsa.shop.style.service.utils;

import com.musinsa.shop.domain.enums.CategoryCode;
import com.musinsa.shop.style.service.exception.CategoryNotFoundException;

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
