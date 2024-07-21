package com.musinsa.shop.dashboard.repository.jpa;

import com.musinsa.shop.domain.entity.CategoryJpaEntity;
import com.musinsa.shop.domain.enums.CategoryCode;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface CategoryJpaRepository extends Repository<CategoryJpaEntity, Long>, ReadOnlyRepository<CategoryJpaEntity, Long> {
    Optional<CategoryJpaEntity> findByCode(CategoryCode code);
}
