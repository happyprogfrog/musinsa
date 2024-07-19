package com.musinsa.shop.dashboard.service.persistence;

import com.musinsa.shop.domain.enums.CategoryCode;
import com.musinsa.shop.domain.model.Category;

import java.util.Optional;

public interface LoadCategoryPort {
    Optional<Category> findCategoryByCode(CategoryCode categoryCode);
}
