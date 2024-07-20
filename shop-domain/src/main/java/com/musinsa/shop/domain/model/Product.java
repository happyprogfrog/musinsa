package com.musinsa.shop.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Product {
    private Long id;
    private Brand brand;
    private Category category;
    private String name;
    private Long price;

    public void update(Brand brand, Category category, String name, Long price) {
        this.brand = brand;
        this.category = category;
        this.name = name;
        this.price = price;
    }
}
