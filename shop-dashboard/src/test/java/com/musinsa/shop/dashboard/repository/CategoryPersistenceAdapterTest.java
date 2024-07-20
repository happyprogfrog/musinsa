package com.musinsa.shop.dashboard.repository;

import com.musinsa.shop.dashboard.repository.jpa.CategoryJpaRepository;
import com.musinsa.shop.dashboard.repository.jpa.entity.CategoryJpaEntityFixtures;
import com.musinsa.shop.domain.enums.CategoryCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CategoryPersistenceAdapterTest {
    private CategoryPersistenceAdapter adapter;

    @Mock
    private CategoryJpaRepository categoryJpaRepository;

    @BeforeEach
    void setUp() {
        adapter = new CategoryPersistenceAdapter(categoryJpaRepository);
    }

    @Test
    @DisplayName("categoryCode로 조회 성공")
    void successFindByCode() {
        // given
        final var categoryJpaEntity = CategoryJpaEntityFixtures.entity();
        given(categoryJpaRepository.findByCode(any()))
                .willReturn(Optional.of(categoryJpaEntity));

        // when
        final var result = adapter.findCategoryByCode(CategoryCode.TOP);

        // then
        then(result)
                .isPresent()
                .hasValueSatisfying(category -> {
                    then(category)
                            .hasFieldOrPropertyWithValue("id", categoryJpaEntity.getId())
                            .hasFieldOrPropertyWithValue("code", categoryJpaEntity.getCode());
                });
    }

    @Test
    @DisplayName("categoryCode로 조회 실패")
    void failFindByCode() {
        // given
        final var categoryJpaEntity = CategoryJpaEntityFixtures.entity();
        given(categoryJpaRepository.findByCode(any()))
                .willReturn(Optional.empty());

        // when
        final var result = adapter.findCategoryByCode(CategoryCode.TOP);

        // then
        then(result)
                .isNotPresent();
    }
}