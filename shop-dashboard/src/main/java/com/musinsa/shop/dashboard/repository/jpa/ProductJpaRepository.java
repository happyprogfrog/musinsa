package com.musinsa.shop.dashboard.repository.jpa;

import com.musinsa.shop.domain.entity.ProductJpaEntity;
import com.musinsa.shop.domain.enums.CategoryCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, Long> {
    List<ProductJpaEntity> findByBrandAlias(String brandAlias);

    List<ProductJpaEntity> findByCategoryCode(CategoryCode categoryCode);
}
