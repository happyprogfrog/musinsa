package com.musinsa.shop.domain.model;

import com.musinsa.shop.domain.enums.CategoryCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

class CategoryTest {

    @Test
    @DisplayName("Category 생성")
    void constructorCategory() {
        // given
        var id = 5L;
        var code = CategoryCode.TOP;

        // when
        var category = Category.builder()
                .id(id)
                .code(code)
                .build();

        // then
        then(category)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("code", code);
    }

    @Test
    @DisplayName("Category 수정")
    void updateCategory() {
        // given
        var id = 5L;
        var code = CategoryCode.TOP;
        var category = Category.builder()
                .id(id)
                .code(code)
                .build();

        // when
        var changedCode = CategoryCode.OUTER;
        category.update(changedCode);

        // then
        then(category)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("code", changedCode);
    }
}