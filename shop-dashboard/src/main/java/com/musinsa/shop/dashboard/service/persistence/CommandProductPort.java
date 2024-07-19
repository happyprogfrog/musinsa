package com.musinsa.shop.dashboard.service.persistence;

import com.musinsa.shop.domain.model.Product;

public interface CommandProductPort {
    Product createProduct(Product product);

    Product modifyProduct(Product product);

    void deleteProduct(Long productId);
}
