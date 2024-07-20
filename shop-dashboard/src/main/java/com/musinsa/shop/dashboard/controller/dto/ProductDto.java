package com.musinsa.shop.dashboard.controller.dto;

import com.musinsa.shop.dashboard.controller.dto.BrandDto.BrandResponse;
import com.musinsa.shop.dashboard.controller.dto.CategoryDto.CategoryResponse;
import com.musinsa.shop.domain.model.Product;
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
            String categoryCodeKey,
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
        String categoryCodeKey,
        @NotBlank
        String name,
        @NotNull
        Long price
    ) {}

    public record ProductResponse(
            @NotNull
            Long id,
            @NotNull
            BrandResponse brand,
            @NotBlank
            CategoryResponse category,
            @NotBlank
            String name,
            @NotNull
            Long price
    ) {
        public static ProductResponse fromDomain(Product product) {
            return new ProductResponse(
                    product.getId(),
                    BrandResponse.fromDomain(product.getBrand()),
                    CategoryResponse.fromDomain(product.getCategory()),
                    product.getName(),
                    product.getPrice()
            );
        }
    }
}
