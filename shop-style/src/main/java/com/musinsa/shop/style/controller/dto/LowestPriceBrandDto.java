package com.musinsa.shop.style.controller.dto;

import com.musinsa.shop.style.controller.utils.PriceFormatter;
import com.musinsa.shop.style.service.dto.ProductPriceDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class LowestPriceBrandDto {
    public record LowestPriceBrandResponse(
            @NotNull
            List<LowestPriceBrandInfo> 제품,
            @NotBlank
            String 총액
    ) {
        public LowestPriceBrandResponse(List<LowestPriceBrandInfo> list, Long totalPrice) {
            this(list, PriceFormatter.convertToKorFormat(totalPrice));
        }
    }

    public record LowestPriceBrandInfo(
            @NotBlank
            String 카테고리,
            @NotBlank
            String 브랜드,
            @NotBlank
            String 가격
    ) {
        public static LowestPriceBrandInfo fromServiceDto(ProductPriceDto dto) {
            return new LowestPriceBrandInfo(
                    dto.categoryCode().getValue(),
                    dto.brand().getName(),
                    PriceFormatter.convertToKorFormat(dto.price())
            );
        }
    }
}
