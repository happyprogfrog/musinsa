package com.musinsa.shop.dashboard.service;

import com.musinsa.shop.dashboard.controller.dto.ProductDto.CreateProductRequest;
import com.musinsa.shop.dashboard.controller.dto.ProductDto.UpdateProductRequest;
import com.musinsa.shop.dashboard.service.exception.BrandNotFoundException;
import com.musinsa.shop.dashboard.service.exception.CategoryNotFoundException;
import com.musinsa.shop.dashboard.service.exception.InvalidPriceException;
import com.musinsa.shop.dashboard.service.exception.ProductNotFoundException;
import com.musinsa.shop.dashboard.service.persistence.CommandProductPort;
import com.musinsa.shop.dashboard.service.persistence.LoadBrandPort;
import com.musinsa.shop.dashboard.service.persistence.LoadCategoryPort;
import com.musinsa.shop.dashboard.service.persistence.LoadProductPort;
import com.musinsa.shop.domain.enums.CategoryCode;
import com.musinsa.shop.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    private ProductService sut;

    @Mock
    private LoadBrandPort loadBrandPort;
    @Mock
    private LoadCategoryPort loadCategoryPort;
    @Mock
    private LoadProductPort loadProductPort;
    @Mock
    private CommandProductPort commandProductPort;

    @BeforeEach
    void setUp() {
        sut = new ProductService(loadBrandPort, loadCategoryPort, loadProductPort, commandProductPort);
    }

    @Nested
    @DisplayName("Product 조회")
    class GetProduct {
        @Test
        @DisplayName("productId로 조회 시 Product 반환")
        void getProductById() {
            // given
            final var product = ProductFixtures.product();
            given(loadProductPort.findProductById(any()))
                    .willReturn(Optional.of(product));

            // when
            final var result = sut.getProductById(10L);

            // then
            then(result)
                    .isNotNull()
                    .hasNoNullFieldsOrProperties()
                    .hasFieldOrPropertyWithValue("id", product.getId())
                    .hasFieldOrPropertyWithValue("brand.id", product.getBrand().getId())
                    .hasFieldOrPropertyWithValue("category.code", product.getCategory().getCode())
                    .hasFieldOrPropertyWithValue("name", product.getName())
                    .hasFieldOrPropertyWithValue("price", product.getPrice());
        }

        @Test
        @DisplayName("brandAlias로 조회 시 Product 목록 반환")
        void getProductsByBrandAlias() {
            // given
            final var brand = BrandFixtures.brand();
            given(loadBrandPort.findBrandByAlias(any()))
                    .willReturn(Optional.of(brand));

            final var product1 = ProductFixtures.product(1L);
            final var product2 = ProductFixtures.product(2L);
            given(loadProductPort.findProductsByBrandAlias(any()))
                    .willReturn(List.of(product1, product2));

            // when
            final var result = sut.getProductsByBrandAlias("alias");

            // then
            then(result).hasSize(2);
        }

        @Test
        @DisplayName("categoryCode로 조회 시 Product 목록 반환")
        void getProductsByCategoryCode() {
            // given
            final var category = CategoryFixtures.categoryOuter();
            given(loadCategoryPort.findCategoryByCode(any()))
                    .willReturn(Optional.of(category));

            final var product1 = ProductFixtures.product(1L);
            final var product2 = ProductFixtures.product(2L);
            given(loadProductPort.findProductsByCategoryCode(any()))
                    .willReturn(List.of(product1, product2));

            // when
            final var result = sut.getProductsByCategoryCode(CategoryCode.TOP.getKey());

            // then
            then(result).hasSize(2);
        }

        @Test
        @DisplayName("productId 조회 시 Product 존재하지 않을 경우 예외 발생")
        void throwProductNotFoundException() {
            // given
            given(loadProductPort.findProductById(any()))
                    .willReturn(Optional.empty());

            // when & then
            thenThrownBy(() -> sut.getProductById(10L))
                    .isInstanceOf(ProductNotFoundException.class);
        }

        @Test
        @DisplayName("brandAlias 조회 시 Brand 존재하지 않을 경우 예외 발생")
        void throwBrandNotFoundException() {
            // given
            given(loadBrandPort.findBrandByAlias(any()))
                    .willReturn(Optional.empty());

            // when & then
            thenThrownBy(() -> sut.getProductsByBrandAlias("alias"))
                    .isInstanceOf(BrandNotFoundException.class);
        }

        @Test
        @DisplayName("categoryCode 조회 시 Category 존재하지 않을 경우 예외 발생")
        void throwCategoryNotFoundException() {
            // given
            given(loadCategoryPort.findCategoryByCode(any()))
                    .willReturn(Optional.empty());

            // when & then
            thenThrownBy(() -> sut.getProductsByCategoryCode(CategoryCode.BAG.getKey()))
                    .isInstanceOf(CategoryNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Product 생성")
    class CreateProduct {
        @Test
        @DisplayName("생성된 Product 확인")
        void returnCreateProduct() {
            // given
            final var request = new CreateProductRequest(1L, CategoryCode.BAG.getKey(), "name", 1000L);

            final var brand = BrandFixtures.brand();
            given(loadBrandPort.findBrandById(any()))
                    .willReturn(Optional.of(brand));

            final var category = CategoryFixtures.categoryTop();
            given(loadCategoryPort.findCategoryByCode(any()))
                    .willReturn(Optional.of(category));

            final var createdProduct = ProductFixtures.product();
            given(commandProductPort.createProduct(any()))
                    .willReturn(createdProduct);

            // when
            final var result = sut.createProduct(request);

            // then
            then(result)
                    .isEqualTo(createdProduct);
        }

        @ParameterizedTest(name = "{0}")
        @CsvSource(
                value = {
                        "name is null, ",
                        "name is empty, ''"
                }
        )
        @DisplayName("정상적이지 않은 name이면 IllegalArgumentException")
        void throwIllegalArgumentException(String desc, String name) {
            final var request = new CreateProductRequest(5L, CategoryCode.BAG.getKey(), name, 1000L);

            thenThrownBy(() -> sut.createProduct(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("정상적이지 않은 price이면 InvalidPriceException")
        void throwInvalidPriceException() {
            final var request = new CreateProductRequest(5L, CategoryCode.BAG.getKey(), "name", -1000L);

            thenThrownBy(() -> sut.createProduct(request))
                    .isInstanceOf(InvalidPriceException.class);
        }
    }

    @Nested
    @DisplayName("Product 변경")
    class ModifyProduct {
        private UpdateProductRequest request;

        @BeforeEach
        void setUp() {
            request = new UpdateProductRequest(1L, 6L, CategoryCode.ACCESSORY.getKey(), "new name", 9999L);
        }

        @Test
        @DisplayName("변경된 Product 확인")
        void returnModifiedProduct() {
            // given
            final var product = ProductFixtures.product();
            given(loadProductPort.findProductById(any()))
                    .willReturn(Optional.of(product));

            final var brand = Brand.builder()
                    .id(6L)
                    .alias("otheralias")
                    .name("other name")
                    .build();
            given(loadBrandPort.findBrandById(any()))
                    .willReturn(Optional.of(brand));

            final var category = Category.builder()
                    .id(8L)
                    .code(CategoryCode.ACCESSORY)
                    .build();
            given(loadCategoryPort.findCategoryByCode(any()))
                    .willReturn(Optional.of(category));

            final var modifiedProduct = Product.builder()
                    .id(brand.getId())
                    .brand(brand)
                    .category(category)
                    .name("new name")
                    .price(9999L)
                    .build();
            given(commandProductPort.modifyProduct(any()))
                    .willReturn(modifiedProduct);

            // when
            final var result = sut.modifyProduct(request);

            // then
            then(result)
                    .isEqualTo(modifiedProduct);
        }
    }

    @Test
    @DisplayName("Product 삭제")
    void deleteProduct() {
        // given
        final var product = ProductFixtures.product();
        given(loadProductPort.findProductById(any()))
                .willReturn(Optional.of(product));
        willDoNothing()
                .given(commandProductPort).deleteProduct(any());

        // when
        sut.deleteProduct(10L);

        // then
        verify(commandProductPort).deleteProduct(10L);
    }
}