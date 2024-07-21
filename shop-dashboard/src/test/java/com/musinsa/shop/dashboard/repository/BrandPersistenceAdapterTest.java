package com.musinsa.shop.dashboard.repository;

import com.musinsa.shop.dashboard.repository.jpa.BrandJpaRepository;
import com.musinsa.shop.domain.entity.BrandJpaEntity;
import com.musinsa.shop.domain.entity.BrandJpaEntityFixtures;
import com.musinsa.shop.domain.model.Brand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BrandPersistenceAdapterTest {
    private BrandPersistenceAdapter adapter;

    @Mock
    private BrandJpaRepository brandJpaRepository;

    @BeforeEach
    void setUp() {
        adapter = new BrandPersistenceAdapter(brandJpaRepository);
    }

    @Test
    @DisplayName("brandId로 Brand 한 개 조회")
    void successFindBrandById() {
        // given
        final var brandJpaEntity = BrandJpaEntityFixtures.entity();
        given(brandJpaRepository.findById(any()))
                .willReturn(Optional.of(brandJpaEntity));

        // when
        final var result = adapter.findBrandById(1L);

        // then
        then(result)
                .isPresent()
                .hasValueSatisfying(brand -> {
                    then(brand)
                            .hasFieldOrPropertyWithValue("id", brandJpaEntity.getId())
                            .hasFieldOrPropertyWithValue("alias", brandJpaEntity.getAlias())
                            .hasFieldOrPropertyWithValue("name", brandJpaEntity.getName());
                });
    }

    @Test
    @DisplayName("brandId로 조회 시 값이 없음")
    void failFindBrandById() {
        // given
        given(brandJpaRepository.findById(any()))
                .willReturn(Optional.empty());

        // when
        final var result = adapter.findBrandById(1L);

        // then
        then(result)
                .isNotPresent();
    }

    @Test
    @DisplayName("전체 브랜드 목록 가져오기")
    void findAllBrands() {
        // given
        final var brandJpaEntity1 = BrandJpaEntityFixtures.entity(1L);
        final var brandJpaEntity2 = BrandJpaEntityFixtures.entity(2L);
        given(brandJpaRepository.findAll())
                .willReturn(List.of(brandJpaEntity1, brandJpaEntity2));

        // when
        final var result = adapter.findAllBrands();

        // then
        then(result)
                .hasSize(2)
                .hasOnlyElementsOfType(Brand.class);
    }

    @Nested
    @DisplayName("Brand 생성")
    class CreateBrand {
        @Captor
        ArgumentCaptor<BrandJpaEntity> captor;

        private final Brand brand = Brand.builder()
                .alias("alias")
                .name("name")
                .build();

        @Test
        @DisplayName("응답값 검증")
        void createBrand_returnCreatedBrand() {
            // given
            final var brandJpaEntity = new BrandJpaEntity("alias", "name");
            ReflectionTestUtils.setField(brandJpaEntity, "id", 1L);
            given(brandJpaRepository.save(any()))
                    .willReturn(brandJpaEntity);

            // when
            final var result = adapter.createBrand(brand);

            // then
            then(result)
                    .hasFieldOrPropertyWithValue("id", 1L)
                    .hasFieldOrPropertyWithValue("alias", "alias")
                    .hasFieldOrPropertyWithValue("name", "name");
        }

        @Test
        @DisplayName("argumentCapture 검증")
        void createBrand_verifySaveArg() {
            // given
            final var brandJpaEntity = new BrandJpaEntity("alias", "name");
            ReflectionTestUtils.setField(brandJpaEntity, "id", 1L);
            given(brandJpaRepository.save(any()))
                    .willReturn(brandJpaEntity);

            // when
            adapter.createBrand(brand);

            // then
            verify(brandJpaRepository).save(captor.capture());
            then(captor.getValue())
                    .hasFieldOrPropertyWithValue("id", null)
                    .hasFieldOrPropertyWithValue("alias", "alias")
                    .hasFieldOrPropertyWithValue("name", "name");
        }
    }

    @Test
    @DisplayName("Brand 변경")
    void modifyBrand() {
        // given
        ArgumentCaptor<BrandJpaEntity> argumentCaptor = ArgumentCaptor.forClass(BrandJpaEntity.class);

        final Brand brand = Brand.builder()
                .id(1L)
                .alias("new alias")
                .name("new name")
                .build();
        final var brandJpaEntity = new BrandJpaEntity("new alias", "new name");
        ReflectionTestUtils.setField(brandJpaEntity, "id", 1L);

        given(brandJpaRepository.save(any(BrandJpaEntity.class)))
                .willReturn(brandJpaEntity);

        // when
        adapter.modifyBrand(brand);

        // then
        verify(brandJpaRepository).save(argumentCaptor.capture());
        then(argumentCaptor.getValue())
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("alias", "new alias")
                .hasFieldOrPropertyWithValue("name", "new name");
    }

    @Test
    @DisplayName("Brand 삭제")
    void deleteBrand() {
        // given
        willDoNothing()
                .given(brandJpaRepository).deleteById(any());

        // when
        adapter.deleteBrand(1L);

        // then
        verify(brandJpaRepository).deleteById(1L);
    }
}