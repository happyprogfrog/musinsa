package com.musinsa.shop.dashboard.repository.jpa;

import com.musinsa.shop.dashboard.repository.jpa.entity.CategoryJpaEntity;
import com.musinsa.shop.domain.enums.CategoryCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CategoryJpaRepositoryTest {
    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        entityManager.persist(new CategoryJpaEntity(CategoryCode.TOP));
        entityManager.persist(new CategoryJpaEntity(CategoryCode.OUTER));
    }

    @Test
    @DisplayName("categoryCode로 조회되는지 확인")
    void successFindByCode() {
        // given
        final var code = CategoryCode.TOP;

        // when
        final var result = categoryJpaRepository.findByCode(code);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getCode()).isEqualTo(code);
    }

    @Test
    @DisplayName("없는 categoryCode로 조회하면, empty를 반환")
    void FailFindByCode() {
        // given
        final var code = CategoryCode.ACCESSORY;

        // when
        final var result = categoryJpaRepository.findByCode(code);

        // then
        assertThat(result).isEmpty();
    }
}