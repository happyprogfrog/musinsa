package com.musinsa.shop.style.controller.dto;

import com.musinsa.shop.domain.model.Category;
import jakarta.validation.constraints.NotBlank;

public record CategoryResponseDto(
        @NotBlank
        String key,
        @NotBlank
        String value
) {
    public static CategoryResponseDto fromDomain(Category category) {
        return new CategoryResponseDto(
                category.getCode().getKey(),
                category.getCode().getValue()
        );
    }
}
