package com.musinsa.shop.dashboard.service.persistence;

import com.musinsa.shop.domain.model.Brand;

import java.util.List;
import java.util.Optional;

public interface LoadBrandPort {
    Optional<Brand> findBrandById(Long brandId);

    Optional<Brand> findBrandByAlias(String alias);

    List<Brand> findAllBrands();
}
