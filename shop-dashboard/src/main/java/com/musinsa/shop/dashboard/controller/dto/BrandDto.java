package com.musinsa.shop.dashboard.controller.dto;

import com.musinsa.shop.domain.model.Brand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BrandDto {
    public record CreateBrandRequest(
            @NotBlank
            String alias,
            @NotBlank
            String name
    ) {}

    public record UpdateBrandRequest(
            @NotNull
            Long id,
            @NotBlank
            String alias,
            @NotBlank
            String name
    ) {}

    public record BrandResponse(
            @NotNull
            Long id,
            @NotBlank
            String alias,
            @NotBlank
            String name
    ) {
        public static BrandResponse fromDomain(Brand brand) {
            return new BrandResponse(
                    brand.getId(),
                    brand.getAlias(),
                    brand.getName()
            );
        }
    }
}
