package com.musinsa.shop.dashboard.repository.jpa;

import com.musinsa.shop.dashboard.repository.jpa.entity.BrandJpaEntity;
import com.musinsa.shop.dashboard.repository.jpa.entity.CategoryJpaEntity;
import com.musinsa.shop.dashboard.repository.jpa.entity.ProductJpaEntity;
import com.musinsa.shop.domain.enums.CategoryCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
class ProductJpaRepositoryTest {
    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private TestEntityManager entityManager;

    private BrandJpaEntity brandJpaEntity;
    private CategoryJpaEntity categoryJpaEntity;

    @BeforeEach
    void setUp() {
        brandJpaEntity = entityManager.persist(new BrandJpaEntity("alias1", "brandName"));
        categoryJpaEntity = entityManager.persist(new CategoryJpaEntity(CategoryCode.TOP));

        entityManager.persist(new ProductJpaEntity(brandJpaEntity, categoryJpaEntity, "name", 1000L));
        entityManager.persist(new ProductJpaEntity(brandJpaEntity, categoryJpaEntity, "name2", 2000L));
    }

    @Test
    @DisplayName("alias로 조회되는지 확인")
    void successFindByAlias() {
        // given
        final var alias = brandJpaEntity.getAlias();

        // when
        final var result = productJpaRepository.findByBrandAlias(alias);

        // then
        then(result)
                .hasSize(2);
    }

    @Test
    @DisplayName("없는 alias로 조회하면 빈 list 반환")
    void failFindByAlias() {
        // given
        final var alias = "temp";

        // when
        final var result = productJpaRepository.findByBrandAlias(alias);

        // then
        then(result)
                .hasSize(0);
    }

    @Test
    @DisplayName("categoryCode로 조회되는지 확인")
    void successFindByCategoryCode() {
        // given
        final var categoryCode = categoryJpaEntity.getCode();

        // when
        final var result = productJpaRepository.findByCategoryCode(categoryCode);

        // then
        then(result)
                .hasSize(2);
    }

    @Test
    @DisplayName("없는 categoryCode로 조회하면 빈 list 반환")
    void failFindByCategoryCode() {
        // given
        final var categoryCode = CategoryCode.BAG;

        // when
        final var result = productJpaRepository.findByCategoryCode(categoryCode);

        // then
        then(result)
                .hasSize(0);
    }
}