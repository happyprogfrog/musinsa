package com.musinsa.shop.style.repository;

import com.musinsa.shop.domain.entity.ProductJpaEntity;
import com.musinsa.shop.domain.enums.CategoryCode;
import com.musinsa.shop.domain.model.Product;
import com.musinsa.shop.style.repository.jpa.ProductJpaRepository;
import com.musinsa.shop.style.service.persistence.LoadProductPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ProductPersistenceAdapter implements LoadProductPort {
    private final ProductJpaRepository productJpaRepository;

    @Override
    public List<Product> findAllByCategoryCodeOrderByPriceAsc(CategoryCode categoryCode) {
        return productJpaRepository.findAllByCategoryCodeOrderByPriceAsc(categoryCode).stream()
                .map(ProductJpaEntity::toDomain)
                .toList();
    }

    @Override
    public List<Product> findAllByBrandIdOrderByCategoryCodeAscAndPriceAsc(Long brandId) {
        return productJpaRepository.findAllByBrandIdOrderByCategoryCodeAscAndPriceAsc(brandId).stream()
                .map(ProductJpaEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Product> findLowestPriceByCategoryCode(CategoryCode categoryCode) {
        return productJpaRepository.findTopByCategoryCodeOrderByPriceAsc(categoryCode)
                .map(ProductJpaEntity::toDomain);
    }

    @Override
    public Optional<Product> findHighestPriceByCategoryCode(CategoryCode categoryCode) {
        return productJpaRepository.findTopByCategoryCodeOrderByPriceDesc(categoryCode)
                .map(ProductJpaEntity::toDomain);
    }
}
