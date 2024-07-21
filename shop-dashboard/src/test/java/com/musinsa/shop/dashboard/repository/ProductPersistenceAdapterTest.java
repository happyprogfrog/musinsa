package com.musinsa.shop.dashboard.repository;

import com.musinsa.shop.dashboard.repository.jpa.ProductJpaRepository;
import com.musinsa.shop.domain.entity.*;
import com.musinsa.shop.domain.enums.CategoryCode;
import com.musinsa.shop.domain.model.Brand;
import com.musinsa.shop.domain.model.Category;
import com.musinsa.shop.domain.model.Product;
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
class ProductPersistenceAdapterTest {
    private ProductPersistenceAdapter adapter;

    @Mock
    private ProductJpaRepository productJpaRepository;

    @BeforeEach
    void setUp() {
        adapter = new ProductPersistenceAdapter(productJpaRepository);
    }

    @Test
    @DisplayName("productId로 Product 한 개 조회")
    void successFindProductById() {
        // given
        final var productJpaEntity = ProductJpaEntityFixtures.entity();
        given(productJpaRepository.findById(any()))
                .willReturn(Optional.of(productJpaEntity));

        // when
        final var result = adapter.findProductById(1L);

        // then
        then(result)
                .isPresent()
                .hasValueSatisfying(product -> {
                    then(product)
                            .hasFieldOrPropertyWithValue("id", productJpaEntity.getId())
                            .hasFieldOrPropertyWithValue("brand.alias", productJpaEntity.getBrand().getAlias())
                            .hasFieldOrPropertyWithValue("category.code", productJpaEntity.getCategory().getCode())
                            .hasFieldOrPropertyWithValue("name", productJpaEntity.getName())
                            .hasFieldOrPropertyWithValue("price", productJpaEntity.getPrice());
                });
    }

    @Test
    @DisplayName("productId로 Product 조회 시 값이 없음")
    void failFindProductById() {
        // given
        given(productJpaRepository.findById(any()))
                .willReturn(Optional.empty());

        // when
        final var result = adapter.findProductById(1L);

        // then
        then(result)
                .isNotPresent();
    }

    @Test
    @DisplayName("alias로 전체 Product 목록 가져오기")
    void successFindProductsByBrandAlias() {
        // given
        final var productJpaEntity1 = ProductJpaEntityFixtures.entity(1L);
        final var productJpaEntity2 = ProductJpaEntityFixtures.entity(2L);
        given(productJpaRepository.findByBrandAlias(any()))
                .willReturn(List.of(productJpaEntity1, productJpaEntity2));

        // when
        final var result = adapter.findProductsByBrandAlias("alias");

        // then
        then(result)
                .hasSize(2)
                .hasOnlyElementsOfType(Product.class);
    }

    @Test
    @DisplayName("categoryCode로 전체 Product 목록 가져오기")
    void successFindProductsByCategoryCode() {
        // given
        final var productJpaEntity1 = ProductJpaEntityFixtures.entity(1L);
        final var productJpaEntity2 = ProductJpaEntityFixtures.entity(2L);
        given(productJpaRepository.findByCategoryCode(any()))
                .willReturn(List.of(productJpaEntity1, productJpaEntity2));

        // when
        final var result = adapter.findProductsByCategoryCode(CategoryCode.TOP);

        // then
        then(result)
                .hasSize(2)
                .hasOnlyElementsOfType(Product.class);
    }

    @Nested
    @DisplayName("Product 생성")
    class CreateProduct {
        @Captor
        ArgumentCaptor<ProductJpaEntity> captor;

        private final Product product = Product.builder()
                .brand(Brand.builder().id(1L).alias("alias1").name("brand1").build())
                .category(Category.builder().id(2L).code(CategoryCode.OUTER).build())
                .name("name")
                .price(2000L)
                .build();

        @Test
        @DisplayName("응답값 검증")
        void createProduct_returnCreatedProduct() {
            // given
            final var brandJpaEntity = BrandJpaEntityFixtures.entity();
            final var categoryJpaEntity = CategoryJpaEntityFixtures.entity();
            final var productJpaEntity = new ProductJpaEntity(
                    brandJpaEntity, categoryJpaEntity, "product name", 1000L
            );
            ReflectionTestUtils.setField(productJpaEntity, "id", 1L);
            given(productJpaRepository.save(any()))
                    .willReturn(productJpaEntity);

            // when
            final var result = adapter.createProduct(product);

            // then
            then(result)
                    .hasFieldOrPropertyWithValue("id", 1L)
                    .hasFieldOrPropertyWithValue("brand.alias", "alias")
                    .hasFieldOrPropertyWithValue("category.code", CategoryCode.TOP)
                    .hasFieldOrPropertyWithValue("name","product name")
                    .hasFieldOrPropertyWithValue("price", 1000L);
        }

        @Test
        @DisplayName("argumentCapture 검증")
        void createProduct_verifySaveArg() {
            // given
            final var brandJpaEntity = BrandJpaEntityFixtures.entity();
            final var categoryJpaEntity = CategoryJpaEntityFixtures.entity();
            final var productJpaEntity = new ProductJpaEntity(
                    brandJpaEntity, categoryJpaEntity, "product name", 1000L
            );
            ReflectionTestUtils.setField(productJpaEntity, "id", 1L);
            given(productJpaRepository.save(any()))
                    .willReturn(productJpaEntity);

            // when
            adapter.createProduct(product);

            // then
            verify(productJpaRepository).save(captor.capture());
            then(captor.getValue())
                    .hasFieldOrPropertyWithValue("id", null)
                    .hasFieldOrPropertyWithValue("brand.alias", "alias1")
                    .hasFieldOrPropertyWithValue("category.code", CategoryCode.OUTER)
                    .hasFieldOrPropertyWithValue("name","name")
                    .hasFieldOrPropertyWithValue("price", 2000L);
        }
    }

    @Nested
    @DisplayName("Product 변경")
    class ModifyProduct {
        @Test
        @DisplayName("응답값 검증")
        void modifyProduct_returnModifiedProduct() {
            // given
            final Product product = Product.builder()
                    .id(2L)
                    .brand(Brand.builder().id(1L).alias("alias1").name("brand1").build())
                    .category(Category.builder().id(2L).code(CategoryCode.OUTER).build())
                    .name("name")
                    .price(2000L)
                    .build();

            final var brandJpaEntity = new BrandJpaEntity("alias", "name");
            ReflectionTestUtils.setField(brandJpaEntity, "id", 6L);

            final var categoryJpaEntity = new CategoryJpaEntity(CategoryCode.ACCESSORY);
            ReflectionTestUtils.setField(categoryJpaEntity, "id", 8L);

            final var productJpaEntity = new ProductJpaEntity(brandJpaEntity, categoryJpaEntity, "new name", 9999L);
            ReflectionTestUtils.setField(productJpaEntity, "id", 10L);
            given(productJpaRepository.save(any()))
                    .willReturn(productJpaEntity);

            // when
            final var result = adapter.modifyProduct(product);

            // then
            then(result)
                    .hasFieldOrPropertyWithValue("id", 10L)
                    .hasFieldOrPropertyWithValue("brand.id", 6L)
                    .hasFieldOrPropertyWithValue("category.id", 8L)
                    .hasFieldOrPropertyWithValue("name", "new name")
                    .hasFieldOrPropertyWithValue("price", 9999L);
        }

        @Test
        @DisplayName("argumentCapture 검증")
        void modifyProduct_verifyModifyArg() {
            // given
            ArgumentCaptor<ProductJpaEntity> argumentCaptor = ArgumentCaptor.forClass(ProductJpaEntity.class);

            final Product product = Product.builder()
                    .id(2L)
                    .brand(Brand.builder().id(1L).alias("alias1").name("brand1").build())
                    .category(Category.builder().id(2L).code(CategoryCode.OUTER).build())
                    .name("name")
                    .price(2000L)
                    .build();

            final var brandJpaEntity = new BrandJpaEntity("alias", "name");
            ReflectionTestUtils.setField(brandJpaEntity, "id", 6L);

            final var categoryJpaEntity = new CategoryJpaEntity(CategoryCode.ACCESSORY);
            ReflectionTestUtils.setField(categoryJpaEntity, "id", 8L);

            final var productJpaEntity = new ProductJpaEntity(brandJpaEntity, categoryJpaEntity, "new name", 9999L);
            ReflectionTestUtils.setField(productJpaEntity, "id", 1L);
            given(productJpaRepository.save(any()))
                    .willReturn(productJpaEntity);

            // when
            adapter.modifyProduct(product);

            // then
            verify(productJpaRepository).save(argumentCaptor.capture());
            then(argumentCaptor.getValue())
                    .hasFieldOrPropertyWithValue("id", 2L)
                    .hasFieldOrPropertyWithValue("brand.id", 1L)
                    .hasFieldOrPropertyWithValue("category.id", 2L)
                    .hasFieldOrPropertyWithValue("name", "name")
                    .hasFieldOrPropertyWithValue("price", 2000L);
        }
    }

    @Test
    @DisplayName("Product 삭제")
    void deleteProduct() {
        // given
        willDoNothing()
                .given(productJpaRepository).deleteById(any());

        // when
        adapter.deleteProduct(1L);

        // then
        verify(productJpaRepository).deleteById(1L);
    }
}