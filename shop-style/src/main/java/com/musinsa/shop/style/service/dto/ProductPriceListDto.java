package com.musinsa.shop.style.service.dto;

import java.util.List;

public record ProductPriceListDto(
        List<ProductPriceDto> dtoList,
        Long totalPrice
) {}
