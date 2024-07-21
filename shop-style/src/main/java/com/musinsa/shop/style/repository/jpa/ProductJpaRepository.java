package com.musinsa.shop.style.repository.jpa;

import com.musinsa.shop.domain.entity.ProductJpaEntity;
import com.musinsa.shop.domain.enums.CategoryCode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductJpaRepository extends Repository<ProductJpaEntity, Long> {
    /**
     * 가격기준으로 오름차순 정렬해서 특정 카테고리의 모든 상품을 가져오기
     */
    @Query("SELECT p FROM ProductJpaEntity p JOIN FETCH p.brand WHERE p.category.code = :categoryCode ORDER BY p.price ASC")
    List<ProductJpaEntity> findAllByCategoryCodeOrderByPriceAsc(@Param("categoryCode") CategoryCode categoryCode);

    /**
     * 카테고리와 가격기준으로 오름차순 정렬해서 특정 브랜드의 모든 상품을 가져오기
     */
    @Query("SELECT p FROM ProductJpaEntity p JOIN FETCH p.brand WHERE p.brand.id = :brandId ORDER BY p.category.code, p.price ASC")
    List<ProductJpaEntity> findAllByBrandIdOrderByCategoryCodeAscAndPriceAsc(@Param("brandId") Long brandId);

    /**
     * 특정 카테고리에서 최저가 상품 1개 가져오기
     */
    @Query("SELECT p FROM ProductJpaEntity p JOIN FETCH p.brand WHERE p.category.code = :categoryCode ORDER BY p.price ASC LIMIT 1")
    Optional<ProductJpaEntity> findTopByCategoryCodeOrderByPriceAsc(@Param("categoryCode") CategoryCode categoryCode);

    /**
     * 특정 카테고리에서 최고가 상품 1개 가져오기
     */
    @Query("SELECT p FROM ProductJpaEntity p JOIN FETCH p.brand WHERE p.category.code = :categoryCode ORDER BY p.price DESC LIMIT 1")
    Optional<ProductJpaEntity> findTopByCategoryCodeOrderByPriceDesc(@Param("categoryCode") CategoryCode categoryCode);
}
