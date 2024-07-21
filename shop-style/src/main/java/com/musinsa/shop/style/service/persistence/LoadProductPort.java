package com.musinsa.shop.style.service.persistence;

import com.musinsa.shop.domain.enums.CategoryCode;
import com.musinsa.shop.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface LoadProductPort {
    List<Product> findAllByCategoryCodeOrderByPriceAsc(CategoryCode categoryCode);

    List<Product> findAllByBrandIdOrderByCategoryCodeAscAndPriceAsc(Long brandId);

    Optional<Product> findLowestPriceByCategoryCode(CategoryCode categoryCode);

    Optional<Product> findHighestPriceByCategoryCode(CategoryCode categoryCode);
}
