package com.musinsa.shop.style.controller.dto;

import com.musinsa.shop.style.controller.utils.PriceFormatter;
import com.musinsa.shop.style.service.dto.ProductPriceDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class LowestTotalPriceBrandDto {
    public record LowestTotalPriceBrandResponse(
            @NotNull
            LowestTotalPriceBrandInfo 최저가
    ) {}

    public record LowestTotalPriceBrandInfo(
            @NotBlank
            String 브랜드,
            @NotNull
            List<LowestTotalPriceBrandDetail> 카테고리,
            @NotBlank
            String 총액
    ) {
        public LowestTotalPriceBrandInfo(String brandName, List<LowestTotalPriceBrandDetail> details, Long totalPrice) {
            this(brandName, details, PriceFormatter.convertToKorFormat(totalPrice));
        }
    }

    public record LowestTotalPriceBrandDetail(
            @NotBlank
            String 카테고리,
            @NotBlank
            String 가격
    ) {
        public static LowestTotalPriceBrandDetail fromServiceDto(ProductPriceDto dto) {
            return new LowestTotalPriceBrandDetail(
                    dto.categoryCode().getValue(),
                    PriceFormatter.convertToKorFormat(dto.price())
            );
        }
    }
}
