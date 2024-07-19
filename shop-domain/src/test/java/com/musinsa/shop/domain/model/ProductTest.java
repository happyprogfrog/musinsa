package com.musinsa.shop.domain.model;

import com.musinsa.shop.domain.enums.CategoryCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

class ProductTest {

    @Test
    @DisplayName("Product 생성")
    void constructorProduct() {
        // given
        var brand = new Brand(10L, "musinsastandard", "무신사 스탠다드");
        var category = new Category(5L, CategoryCode.TOP);

        // when
        var product = Product.builder()
                .id(1L)
                .brand(brand)
                .category(category)
                .name("릴렉스드 린넨 라이크 셔츠")
                .price(39900L)
                .build();

        // then
        then(product)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("brand.id", 10L)
                .hasFieldOrPropertyWithValue("category.id", 5L)
                .hasFieldOrPropertyWithValue("name", "릴렉스드 린넨 라이크 셔츠")
                .hasFieldOrPropertyWithValue("price", 39900L);
    }

    @Test
    @DisplayName("Product 수정")
    void updateProduct() {
        // given
        var brand = new Brand(10L, "musinsastandard", "무신사 스탠다드");
        var category = new Category(5L, CategoryCode.TOP);
        var product = Product.builder()
                .id(1L)
                .brand(brand)
                .category(category)
                .name("릴렉스드 린넨 라이크 셔츠")
                .price(39900L)
                .build();

        // when
        var changedBrand = new Brand(11L, "musinsastandardwoman", "무신사 스탠다드 우먼");
        var changedCategory = new Category(6L, CategoryCode.ACCESSORY);
        var changedName = "우먼즈 니트 레그 워머";
        var changedPrice = 43900L;
        product.update(changedBrand, changedCategory, changedName, changedPrice);

        // then
        then(product)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("brand.id", 11L)
                .hasFieldOrPropertyWithValue("category.id", 6L)
                .hasFieldOrPropertyWithValue("name", "우먼즈 니트 레그 워머")
                .hasFieldOrPropertyWithValue("price", 43900L);
    }
}