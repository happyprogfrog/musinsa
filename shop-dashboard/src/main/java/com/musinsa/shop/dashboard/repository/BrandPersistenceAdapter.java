package com.musinsa.shop.dashboard.repository;

import com.musinsa.shop.dashboard.repository.jpa.BrandJpaRepository;
import com.musinsa.shop.dashboard.repository.jpa.entity.BrandJpaEntity;
import com.musinsa.shop.dashboard.service.persistence.CommandBrandPort;
import com.musinsa.shop.dashboard.service.persistence.LoadBrandPort;
import com.musinsa.shop.domain.model.Brand;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Component
public class BrandPersistenceAdapter implements LoadBrandPort, CommandBrandPort {
    private final BrandJpaRepository brandJpaRepository;

    @Override
    public Optional<Brand> findBrandById(Long brandId) {
        return brandJpaRepository.findById(brandId)
                .map(BrandJpaEntity::toDomain);
    }

    @Override
    public Optional<Brand> findBrandByAlias(String alias) {
        return brandJpaRepository.findByAlias(alias)
                .map(BrandJpaEntity::toDomain);
    }

    @Override
    public List<Brand> findAllBrands() {
        return brandJpaRepository.findAll().stream()
                .map(BrandJpaEntity::toDomain)
                .toList();
    }

    @Override
    public Brand createBrand(Brand brand) {
        BrandJpaEntity brandJpaEntity = brandJpaRepository.save(BrandJpaEntity.fromDomain(brand));

        return brandJpaEntity.toDomain();
    }

    @Override
    public Brand modifyBrand(Brand brand) {
        BrandJpaEntity brandJpaEntity = brandJpaRepository.save(BrandJpaEntity.fromDomain(brand));

        return brandJpaEntity.toDomain();
    }

    @Override
    public void deleteBrand(Long brandId) {
        brandJpaRepository.deleteById(brandId);
    }
}
