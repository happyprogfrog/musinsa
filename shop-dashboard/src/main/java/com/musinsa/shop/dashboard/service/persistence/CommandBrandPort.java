package com.musinsa.shop.dashboard.service.persistence;

import com.musinsa.shop.domain.model.Brand;

public interface CommandBrandPort {
    Brand createBrand(Brand brand);

    Brand modifyBrand(Brand brand);

    void deleteBrand(Long brandId);
}
