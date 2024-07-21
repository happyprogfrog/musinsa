package com.musinsa.shop.style.controller.dto;

import com.musinsa.shop.style.controller.utils.PriceFormatter;
import com.musinsa.shop.style.service.dto.ProductPriceDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ExtremePriceDto {
    public record ExtremePriceResponse(
            @NotBlank
            String 카테고리,
            @NotNull
            List<ExtremePriceInfo> 최저가,
            @NotNull
            List<ExtremePriceInfo> 최고가
    ) {}

    public record ExtremePriceInfo(
            @NotBlank
            String 브랜드,
            @NotBlank
            String 가격
    ) {
        public static ExtremePriceInfo fromServiceDto(ProductPriceDto dto) {
            return new ExtremePriceInfo(
                    dto.brand().getName(),
                    PriceFormatter.convertToKorFormat(dto.price())
            );
        }
    }
}
