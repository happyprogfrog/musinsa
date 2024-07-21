package com.musinsa.shop.style.controller;

import com.musinsa.shop.style.common.controller.ApiResponse;
import com.musinsa.shop.style.controller.dto.CategoryResponseDto;
import com.musinsa.shop.style.controller.dto.ExtremePriceDto.ExtremePriceInfo;
import com.musinsa.shop.style.controller.dto.ExtremePriceDto.ExtremePriceResponse;
import com.musinsa.shop.style.controller.dto.LowestPriceBrandDto.LowestPriceBrandInfo;
import com.musinsa.shop.style.controller.dto.LowestPriceBrandDto.LowestPriceBrandResponse;
import com.musinsa.shop.style.controller.dto.LowestTotalPriceBrandDto.LowestTotalPriceBrandResponse;
import com.musinsa.shop.style.service.CategoryService;
import com.musinsa.shop.style.service.ProductService;
import com.musinsa.shop.style.service.dto.ProductLowestPriceListDto;
import com.musinsa.shop.style.service.dto.ProductPriceDto;
import com.musinsa.shop.style.service.dto.ProductPriceListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.musinsa.shop.style.controller.dto.LowestTotalPriceBrandDto.LowestTotalPriceBrandDetail;
import static com.musinsa.shop.style.controller.dto.LowestTotalPriceBrandDto.LowestTotalPriceBrandInfo;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/styles")
public class StyleController {
    private final CategoryService categoryService;
    private final ProductService productService;

    @GetMapping("/categories")
    ApiResponse<List<CategoryResponseDto>> getAllCategories() {
        List<CategoryResponseDto> categoryResponse = categoryService.getAllCategories().stream()
                .map(CategoryResponseDto::fromDomain)
                .toList();

        return ApiResponse.just(categoryResponse);
    }

    @GetMapping("/lowest-price-category")
    LowestPriceBrandResponse getLowestPriceBrandInfoByCategory() {
        ProductPriceListDto lowestPriceBrandByCategory = productService.getLowestPriceBrandInfoByCategory();

        List<LowestPriceBrandInfo> list = lowestPriceBrandByCategory.dtoList().stream()
                .map(LowestPriceBrandInfo::fromServiceDto)
                .toList();

        return new LowestPriceBrandResponse(list, lowestPriceBrandByCategory.totalPrice());
    }

    @GetMapping("/lowest-total-price-brand")
    LowestTotalPriceBrandResponse getLowestTotalPriceInfoByBrand() {
        ProductLowestPriceListDto lowestProductPriceList = productService.getLowestTotalPriceInfoByBrand();

        List<LowestTotalPriceBrandDetail> lowestTotalPriceBrandDetailList = lowestProductPriceList.dtoList().stream()
                .map(LowestTotalPriceBrandDetail::fromServiceDto)
                .toList();

        return new LowestTotalPriceBrandResponse(
                new LowestTotalPriceBrandInfo(
                        lowestProductPriceList.brand().getName(),
                        lowestTotalPriceBrandDetailList,
                        lowestProductPriceList.totalPrice()
                )
        );
    }

    @GetMapping("/categories/categoryCode={categoryCode}")
    ExtremePriceResponse getExtremePriceInfoByCategoryCode(@PathVariable(name = "categoryCode") String categoryCodeKey) {
        ProductPriceDto lowestPriceDto = productService.getLowestPriceInfoByCategoryCode(categoryCodeKey);
        ProductPriceDto highestPriceDto = productService.getHighestPriceInfoByCategoryCode(categoryCodeKey);

        return new ExtremePriceResponse(
                lowestPriceDto.categoryCode().getValue(),
                List.of(ExtremePriceInfo.fromServiceDto(lowestPriceDto)),
                List.of(ExtremePriceInfo.fromServiceDto(highestPriceDto)));
    }
}
