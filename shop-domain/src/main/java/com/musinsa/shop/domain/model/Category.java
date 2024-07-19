package com.musinsa.shop.domain.model;

import com.musinsa.shop.domain.enums.CategoryCode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Category {
    private Long id;
    private CategoryCode code;

    public void update(CategoryCode code) {
        this.code = code;
    }
}
