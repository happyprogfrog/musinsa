package com.musinsa.shop.style.repository;

import com.musinsa.shop.domain.entity.BrandJpaEntity;
import com.musinsa.shop.domain.model.Brand;
import com.musinsa.shop.style.repository.jpa.BrandJpaRepository;
import com.musinsa.shop.style.service.persistence.LoadBrandPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class BrandPersistenceAdapter implements LoadBrandPort {
    private final BrandJpaRepository brandJpaRepository;

    @Override
    public List<Brand> findAllBrands() {
        return brandJpaRepository.findAll().stream()
                .map(BrandJpaEntity::toDomain)
                .toList();
    }
}
