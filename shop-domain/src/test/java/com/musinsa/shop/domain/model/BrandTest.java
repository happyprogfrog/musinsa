package com.musinsa.shop.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

class BrandTest {

    @Test
    @DisplayName("Brand 생성")
    void constructorBrand() {
        // given
        final var id = 10L;
        final var alias = "musinsastandard";
        final var name = "무신사 스탠다드";

        // when
        final var brand = Brand.builder()
                .id(id)
                .alias(alias)
                .name(name)
                .build();

        // then
        then(brand)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("alias", alias)
                .hasFieldOrPropertyWithValue("name", name);
    }

    @Test
    @DisplayName("Brand 수정")
    void updateBrand() {
        // given
        final var id = 10L;
        final var alias = "musinsastandar";
        final var name = "무신사 스탱다드";
        final var brand = Brand.builder()
                .id(id)
                .alias(alias)
                .name(name)
                .build();

        // when
        final var changedAlias = "musinsastandard";
        final var changedName = "무신사 스탠다드";
        brand.update(changedAlias, changedName);

        // then
        then(brand)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("alias", changedAlias)
                .hasFieldOrPropertyWithValue("name", changedName);
    }
}