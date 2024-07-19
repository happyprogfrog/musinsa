package com.musinsa.shop.dashboard.repository.jpa;

import com.musinsa.shop.dashboard.repository.jpa.entity.BrandJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandJpaRepository extends JpaRepository<BrandJpaEntity, Long> {
    Optional<BrandJpaEntity> findByAlias(String alias);
}
