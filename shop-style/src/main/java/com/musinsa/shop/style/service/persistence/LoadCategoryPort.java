package com.musinsa.shop.style.service.persistence;

import com.musinsa.shop.domain.model.Category;

import java.util.List;

public interface LoadCategoryPort {
    List<Category> findAllCategories();
}
