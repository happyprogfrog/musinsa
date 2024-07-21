package com.musinsa.shop.style.service.persistence;

import com.musinsa.shop.domain.model.Brand;

import java.util.List;

public interface LoadBrandPort {
    List<Brand> findAllBrands();
}
