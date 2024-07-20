package com.musinsa.shop.dashboard.service.persistence;

import com.musinsa.shop.domain.enums.CategoryCode;
import com.musinsa.shop.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface LoadProductPort {
    Optional<Product> findProductById(Long productId);

    List<Product> findProductsByBrandAlias(String brandAlias);

    List<Product> findProductsByCategoryCode(CategoryCode categoryCode);
}
