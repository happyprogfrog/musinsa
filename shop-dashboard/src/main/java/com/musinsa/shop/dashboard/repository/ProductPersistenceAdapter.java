package com.musinsa.shop.dashboard.repository;

import com.musinsa.shop.dashboard.repository.jpa.ProductJpaRepository;
import com.musinsa.shop.dashboard.service.persistence.CommandProductPort;
import com.musinsa.shop.dashboard.service.persistence.LoadProductPort;
import com.musinsa.shop.domain.entity.ProductJpaEntity;
import com.musinsa.shop.domain.enums.CategoryCode;
import com.musinsa.shop.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ProductPersistenceAdapter implements LoadProductPort, CommandProductPort {
    private final ProductJpaRepository productJpaRepository;

    @Override
    public Optional<Product> findProductById(Long productId) {
        return productJpaRepository.findById(productId)
                .map(ProductJpaEntity::toDomain);
    }

    @Override
    public List<Product> findProductsByBrandAlias(String brandAlias) {
        return productJpaRepository.findByBrandAlias(brandAlias).stream()
                .map(ProductJpaEntity::toDomain)
                .toList();
    }

    @Override
    public List<Product> findProductsByCategoryCode(CategoryCode categoryCode) {
        return productJpaRepository.findByCategoryCode(categoryCode).stream()
                .map(ProductJpaEntity::toDomain)
                .toList();
    }

    @Override
    public Product createProduct(Product product) {
        ProductJpaEntity productJpaEntity = productJpaRepository.save(ProductJpaEntity.fromDomain(product));

        return productJpaEntity.toDomain();
    }

    @Override
    public Product modifyProduct(Product product) {
        ProductJpaEntity productJpaEntity = productJpaRepository.save(ProductJpaEntity.fromDomain(product));

        return productJpaEntity.toDomain();
    }

    @Override
    public void deleteProduct(Long productId) {
        productJpaRepository.deleteById(productId);
    }
}
