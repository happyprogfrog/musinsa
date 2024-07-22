package com.musinsa.shop.style.repository.jpa;

import com.musinsa.shop.domain.entity.CategoryJpaEntity;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface CategoryJpaRepository extends Repository<CategoryJpaEntity, Long>, ReadOnlyRepository<CategoryJpaEntity, Long> {
    List<CategoryJpaEntity> findAllByOrderByCodeAsc();
}
