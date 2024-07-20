package com.musinsa.shop.dashboard.controller.dto;

import com.musinsa.shop.domain.model.Category;
import jakarta.validation.constraints.NotBlank;

public class CategoryDto {
    public record CategoryResponse(
            @NotBlank
            String key,
            @NotBlank
            String value
    ) {
         public static CategoryResponse of(Category category) {
            return new CategoryResponse(
                    category.getCode().getKey(),
                    category.getCode().getValue()
            );
        }
    }
}
