package com.musinsa.shop.dashboard.repository.jpa;

import com.musinsa.shop.domain.entity.BrandJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BrandJpaRepositoryTest {
    @Autowired
    private BrandJpaRepository brandJpaRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        entityManager.persist(new BrandJpaEntity("alias1", "name1"));
        entityManager.persist(new BrandJpaEntity("alias2", "name2"));
    }

    @Test
    @DisplayName("alias로 조회되는지 확인")
    void successFindByAlias() {
        // given
        final var alias = "alias1";

        // when
        final var result = brandJpaRepository.findByAlias(alias);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getAlias()).isEqualTo(alias);
    }

    @Test
    @DisplayName("없는 alias로 조회하면, empty를 반환")
    void failFindByAlias() {
        // given
        final var alias = "alias3";

        // when
        final var result = brandJpaRepository.findByAlias(alias);

        // then
        assertThat(result).isEmpty();
    }
}