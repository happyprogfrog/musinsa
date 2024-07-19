package com.musinsa.shop.dashboard.controller.dto;

import com.musinsa.shop.domain.enums.CategoryCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductDto {
    public record CreateProductRequest(
            @NotNull
            Long brandId,
            @NotBlank
            CategoryCode categoryCode,
            @NotBlank
            String name,
            @NotNull
            Long price
    ) {}

    public record UpdateProductRequest(
        @NotNull
        Long id,
        @NotNull
        Long brandId,
        @NotBlank
        CategoryCode categoryCode,
        @NotBlank
        String name,
        @NotNull
        Long price
    ) {}
}
