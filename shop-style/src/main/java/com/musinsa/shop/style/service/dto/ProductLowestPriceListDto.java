package com.musinsa.shop.style.service.dto;

import com.musinsa.shop.domain.model.Brand;

import java.util.List;

public record ProductLowestPriceListDto(
        Brand brand,
        List<ProductPriceDto> dtoList,
        Long totalPrice
) {}
