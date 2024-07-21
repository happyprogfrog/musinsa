package com.musinsa.shop.style.service;

import com.musinsa.shop.domain.model.Brand;
import com.musinsa.shop.style.service.persistence.LoadBrandPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class BrandService {
    private final LoadBrandPort loadBrandPort;

    @Transactional(readOnly = true)
    public List<Brand> getAllBrands() {
        return loadBrandPort.findAllBrands();
    }
}
