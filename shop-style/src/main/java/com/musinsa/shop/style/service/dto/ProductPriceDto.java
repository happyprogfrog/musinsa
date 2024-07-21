package com.musinsa.shop.style.service.dto;

import com.musinsa.shop.domain.enums.CategoryCode;
import com.musinsa.shop.domain.model.Brand;
import com.musinsa.shop.domain.model.Product;

public record ProductPriceDto(
        Brand brand,
        CategoryCode categoryCode,
        String name,
        Long price
) {
    public static ProductPriceDto fromDomain(Product product) {
        return new ProductPriceDto(
                product.getBrand(),
                product.getCategory().getCode(),
                product.getName(),
                product.getPrice()
        );
    }
}
