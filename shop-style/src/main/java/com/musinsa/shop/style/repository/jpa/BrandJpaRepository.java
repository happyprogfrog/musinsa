package com.musinsa.shop.style.repository.jpa;

import com.musinsa.shop.domain.entity.BrandJpaEntity;
import org.springframework.data.repository.Repository;

public interface BrandJpaRepository extends Repository<BrandJpaEntity, Long>, ReadOnlyRepository<BrandJpaEntity, Long> {
}
